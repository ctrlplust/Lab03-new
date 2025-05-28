package com.example.gamedata;

import com.example.gamedata.Game;
import com.example.gamedata.Dataset;
import com.example.gamedata.GenerateData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class BenchmarkRunner {

    private static final int NUMBER_OF_RUNS = 3;
    private static final List<String> ALGORITHMS_TO_TEST = Arrays.asList(
            "bubbleSort", "insertionSort", "selectionSort", "mergeSort", "quickSort", "collections.sort"
    );
    private static final List<String> ATTRIBUTES_TO_SORT_BY = Arrays.asList(
            "price", "category", "quality"
    );
    private static final int[] DATASET_SIZES_N = {100, 10000, 1000000};
    private static final long SINGLE_RUN_TIMEOUT_SECONDS = 300; // 5 minutos

    static java.util.Map<String, java.util.Map<String, java.util.Map<Integer, Double>>> results = new java.util.HashMap<>();

    public static void main(String[] args) {
        System.out.println("Iniciando Benchmarks de Ordenamiento...");
        System.out.println("Número de ejecuciones por prueba para promediar: " + NUMBER_OF_RUNS);
        System.out.println("Timeout por ejecución individual: " + SINGLE_RUN_TIMEOUT_SECONDS + " segundos.");

        for (String attribute : ATTRIBUTES_TO_SORT_BY) {
            results.put(attribute, new java.util.HashMap<>());
            for (String algorithm : ALGORITHMS_TO_TEST) {
                results.get(attribute).put(algorithm, new java.util.HashMap<>());
            }
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (String attribute : ATTRIBUTES_TO_SORT_BY) {
            System.out.println(" ATRIBUTO DE ORDENAMIENTO: " + attribute.toUpperCase());
            System.out.println("=========================================================");

            for (String algorithm : ALGORITHMS_TO_TEST) {
                System.out.println("\n--- Algoritmo: " + algorithm + " (Atributo: " + attribute + ") ---");

                for (int N : DATASET_SIZES_N) {
                    if (N == 1_000_000 && (algorithm.equals("bubbleSort") || algorithm.equals("insertionSort") || algorithm.equals("selectionSort"))) {
                        System.out.println("  [SKIPPED] " + algorithm + " es demasiado lento para N = " + N + " (timeout > " + SINGLE_RUN_TIMEOUT_SECONDS + " seg)");
                        results.get(attribute).get(algorithm).put(N, -2.0);
                        continue;
                    }

                    System.out.print("  Procesando N = " + N + "... ");
                    ArrayList<Game> baseGeneratedGames = loadOrGenerateGames(N, attribute);

                    if (baseGeneratedGames == null || (baseGeneratedGames.isEmpty() && N > 0)) {
                        System.out.println("FALLO al cargar/generar. Saltando.");
                        results.get(attribute).get(algorithm).put(N, -2.0);
                        continue;
                    }

                    long totalDurationNs = 0;
                    int successfulRuns = 0;
                    boolean timeoutOccurredOverall = false;

                    for (int i = 0; i < NUMBER_OF_RUNS; i++) {
                        final ArrayList<Game> gamesToSort = new ArrayList<>(baseGeneratedGames);
                        final Dataset dataset = new Dataset(gamesToSort);

                        Callable<Long> task = () -> {
                            long startTime = System.nanoTime();
                            dataset.sortByAlgorithm(algorithm, attribute, false);
                            long endTime = System.nanoTime();
                            return (endTime - startTime);
                        };

                        Future<Long> future = executor.submit(task);
                        long durationNsThisRun = -1;

                        try {
                            durationNsThisRun = future.get(SINGLE_RUN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                            totalDurationNs += durationNsThisRun;
                            successfulRuns++;
                        } catch (TimeoutException e) {
                            future.cancel(true);
                            System.out.print("[TIMEOUT en run " + (i + 1) + "] ");
                            timeoutOccurredOverall = true;
                            break;
                        } catch (InterruptedException | ExecutionException e) {
                            future.cancel(true);
                            System.out.print("[ERROR en run " + (i + 1) + ": " + e.getMessage() + "] ");
                            timeoutOccurredOverall = true;
                            break;
                        }
                    }

                    if (timeoutOccurredOverall) {
                        System.out.println(" Resultado: >" + SINGLE_RUN_TIMEOUT_SECONDS * 1000 + " ms (timeout o error)");
                        results.get(attribute).get(algorithm).put(N, -1.0);
                    } else if (successfulRuns > 0) {
                        double averageDurationMs = (double) totalDurationNs / successfulRuns / 1_000_000.0;
                        System.out.printf(" Tiempo Promedio: %.3f ms%n", averageDurationMs);
                        results.get(attribute).get(algorithm).put(N, averageDurationMs);
                    } else {
                        System.out.println(" No se completaron ejecuciones exitosas.");
                        results.get(attribute).get(algorithm).put(N, -2.0);
                    }
                }
            }
        }

        executor.shutdownNow();
        printResultsTable();
        System.out.println("\n--- Benchmarks Completados ---");

        ArrayList<Game> games = GenerateData.generateRandomGames(100);
        Dataset dataset = new Dataset(games);
        long start = System.nanoTime();
        dataset.sortByAlgorithm("bubbleSort", "quality", true);
        long end = System.nanoTime();
        System.out.println("Tiempo BubbleSort N=100: " + (end - start) / 1_000_000.0 + " ms");
    }

    private static ArrayList<Game> loadOrGenerateGames(int N, String attributeForContext) {
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
                    if (timeMs == null) {
                        timeStr = "ERROR/NO DATA";
                    } else if (timeMs == -2.0) {
                        timeStr = "SKIPPED";
                    } else if (timeMs == -1.0) {
                        timeStr = ">" + (SINGLE_RUN_TIMEOUT_SECONDS * 1000);
                    } else {
                        timeStr = String.format("%.3f", timeMs);
                    }

                    String nStr = (N == 100) ? "10^2" : (N == 10000) ? "10^4" : (N == 1000000) ? "10^6" : String.valueOf(N);
                    System.out.printf("| %-20s | %-20s | %-25s |\n", algorithm, nStr, timeStr);
                }
                if (DATASET_SIZES_N.length > 0 && !algorithm.equals(ALGORITHMS_TO_TEST.get(ALGORITHMS_TO_TEST.size() - 1))) {
                    System.out.println("--------------------------------------------------------------------------");
                }
            }
            System.out.println("--------------------------------------------------------------------------");
        }
    }
}
