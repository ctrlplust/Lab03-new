package com.example.gamedata; // O tu paquete

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*; // Para el timeout

public class BenchmarkRunner {

    private static final int NUMBER_OF_RUNS = 3;
    private static final List<String> ALGORITHMS_TO_TEST = Arrays.asList(
            "bubbleSort", "insertionSort", "selectionSort", "mergeSort", "quickSort", "collections.sort"
    );
    private static final List<String> ATTRIBUTES_TO_SORT_BY = Arrays.asList(
            "price", "category", "quality"
    );

    // Tamaños de N como se especifica: 10^2, 10^4, 10^6
    // ¡CUIDADO! 10^6 (1,000,000) será MUY lento para O(N^2) como BubbleSort, InsertionSort, SelectionSort.
    // Es probable que estos superen los 300 segundos.
    private static final int[] DATASET_SIZES_N = {100, 10000}; // Empezamos con estos, luego añadimos 1_000_000
    // private static final int[] DATASET_SIZES_N = {100, 10000, 1000000}; // Para la prueba completa

    // Timeout en segundos para cada ejecución individual de un algoritmo
    private static final long SINGLE_RUN_TIMEOUT_SECONDS = 300; // 300 segundos = 5 minutos


    // Estructura para almacenar los resultados para facilitar la impresión de la tabla
    // Map<Atributo, Map<Algoritmo, Map<TamañoN, TiempoPromedioMs>>>
    static java.util.Map<String, java.util.Map<String, java.util.Map<Integer, Double>>> results = new java.util.HashMap<>();


    public static void main(String[] args) {
        System.out.println("Iniciando Benchmarks de Ordenamiento...");
        System.out.println("Número de ejecuciones por prueba para promediar: " + NUMBER_OF_RUNS);
        System.out.println("Timeout por ejecución individual: " + SINGLE_RUN_TIMEOUT_SECONDS + " segundos.");

        // Inicializar la estructura de resultados
        for (String attribute : ATTRIBUTES_TO_SORT_BY) {
            results.put(attribute, new java.util.HashMap<>());
            for (String algorithm : ALGORITHMS_TO_TEST) {
                results.get(attribute).put(algorithm, new java.util.HashMap<>());
            }
        }
        
        // ExecutorService para manejar timeouts
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (String attribute : ATTRIBUTES_TO_SORT_BY) {
            System.out.println("\n=========================================================");
            System.out.println(" ATRIBUTO DE ORDENAMIENTO: " + attribute.toUpperCase());
            System.out.println("=========================================================");

            for (String algorithm : ALGORITHMS_TO_TEST) {
                System.out.println("\n--- Algoritmo: " + algorithm + " (Atributo: " + attribute + ") ---");

                for (int N : DATASET_SIZES_N) {
                    System.out.print("  Procesando N = " + N + "... ");

                    // Cargar o generar dataset base para este N
                    // Para benchmarks consistentes, es mejor cargar desde los archivos generados previamente.
                    // Si no, generar uno nuevo cada vez es aceptable, pero los datos serán diferentes entre atributos.
                    // Por ahora, seguiremos generando para simplificar el runner.
                    // Idealmente, aquí se leería el archivo "games_N" + N + ".csv"
                    ArrayList<Game> baseGeneratedGames = loadOrGenerateGames(N, attribute); // Nueva función para cargar/generar
                     if (baseGeneratedGames == null || (baseGeneratedGames.isEmpty() && N > 0)) {
                        System.out.println("FALLO al cargar/generar. Saltando.");
                        results.get(attribute).get(algorithm).put(N, -2.0); // Indicador de error/no datos
                        continue;
                    }


                    long totalDurationNs = 0;
                    int successfulRuns = 0;
                    boolean timeoutOccurredOverall = false;

                    for (int i = 0; i < NUMBER_OF_RUNS; i++) {
                        final ArrayList<Game> gamesToSort = new ArrayList<>(baseGeneratedGames); // COPIA FRESCA
                        final Dataset dataset = new Dataset(gamesToSort);
                        
                        Callable<Long> task = () -> {
                            long startTime = System.nanoTime();
                            dataset.sortByAlgorithm(algorithm, attribute, false); // false para no imprimir desde Dataset
                            long endTime = System.nanoTime();
                            return (endTime - startTime);
                        };

                        Future<Long> future = executor.submit(task);
                        long durationNsThisRun = -1; // Default a -1 (timeout/error)

                        try {
                            // Esperar por el resultado con timeout
                            durationNsThisRun = future.get(SINGLE_RUN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                            totalDurationNs += durationNsThisRun;
                            successfulRuns++;
                            // System.out.println("    Run " + (i+1) + ": " + durationNsThisRun / 1_000_000.0 + " ms");
                        } catch (TimeoutException e) {
                            future.cancel(true); // Interrumpir la tarea si es posible
                            System.out.print("[TIMEOUT en run " + (i+1) + "] ");
                            timeoutOccurredOverall = true;
                            break; // Salir del bucle de repeticiones para este N si una ejecución falla por timeout
                        } catch (InterruptedException | ExecutionException e) {
                            future.cancel(true);
                            System.out.print("[ERROR en run " + (i+1) + ": " + e.getMessage() + "] ");
                            timeoutOccurredOverall = true; // Considerar como fallo
                            break; 
                        }
                    } // Fin del bucle de NUMBER_OF_RUNS

                    if (timeoutOccurredOverall) {
                        System.out.println(" Resultado: >" + SINGLE_RUN_TIMEOUT_SECONDS * 1000 + " ms (timeout o error)");
                        results.get(attribute).get(algorithm).put(N, -1.0); // Usar -1.0 para indicar timeout
                    } else if (successfulRuns > 0) {
                        double averageDurationMs = (double) totalDurationNs / successfulRuns / 1_000_000.0;
                        System.out.printf(" Tiempo Promedio: %.3f ms%n", averageDurationMs);
                        results.get(attribute).get(algorithm).put(N, averageDurationMs);
                    } else {
                         System.out.println(" No se completaron ejecuciones exitosas.");
                         results.get(attribute).get(algorithm).put(N, -2.0); // Error general
                    }
                } // Fin del bucle de DATASET_SIZES_N
            } // Fin del bucle de ALGORITHMS_TO_TEST
        } // Fin del bucle de ATTRIBUTES_TO_SORT_BY
        
        executor.shutdownNow(); // Asegurarse de que el executor se cierre

        printResultsTable();

        System.out.println("\n--- Benchmarks Completados ---");
    }

    // Función placeholder para cargar datos. Deberías implementar la lectura de CSV aquí.
    private static ArrayList<Game> loadOrGenerateGames(int N, String attributeForContext) {
        // TODO: Implementar la lectura desde los archivos CSV generados por GenerateData.
        // Por ejemplo: "data_generated/games_N" + N + ".csv"
        // Si el archivo no existe o hay un error, entonces generar.
        // Por ahora, solo generamos:
        // System.out.println("    (Generando datos para N=" + N + " para atributo " + attributeForContext + ")");
        return GenerateData.generateRandomGames(N);
    }


    private static void printResultsTable() {
        System.out.println("\n\n--- TABLAS DE RESULTADOS (Tiempo Promedio en Milisegundos) ---");

        for (String attribute : ATTRIBUTES_TO_SORT_BY) {
            System.out.println("\nCuadro: Tiempos de ejecución de ordenamiento para el atributo " + attribute.toUpperCase());
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("| %-20s | %-20s | %-25s |\n", "Algoritmo", "Tamaño del dataset", "Tiempo Promedio (ms)");
            System.out.println("--------------------------------------------------------------------------");

            for (String algorithm : ALGORITHMS_TO_TEST) {
                for (int N : DATASET_SIZES_N) {
                    Double timeMs = results.get(attribute).get(algorithm).get(N);
                    String timeStr;
                    if (timeMs == null || timeMs == -2.0) { // -2 para error/no datos
                        timeStr = "ERROR/NO DATA";
                    } else if (timeMs == -1.0) { // -1 para timeout
                        timeStr = ">" + (SINGLE_RUN_TIMEOUT_SECONDS * 1000);
                    } else {
                        timeStr = String.format("%.3f", timeMs);
                    }
                    // Formatear N para que coincida con 10^X
                    String nStr = "";
                    if (N == 100) nStr = "10^2";
                    else if (N == 10000) nStr = "10^4";
                    else if (N == 1000000) nStr = "10^6";
                    else nStr = String.valueOf(N);
                    
                    System.out.printf("| %-20s | %-20s | %-25s |\n", algorithm, nStr, timeStr);
                }
                 if (Arrays.asList(DATASET_SIZES_N).size() > 0 && !algorithm.equals(ALGORITHMS_TO_TEST.get(ALGORITHMS_TO_TEST.size() -1))) { // No imprimir línea extra al final
                    // System.out.println("|----------------------|----------------------|---------------------------|"); // Línea separadora entre algoritmos si se desea
                 }
            }
            System.out.println("--------------------------------------------------------------------------");
        }
    }
}