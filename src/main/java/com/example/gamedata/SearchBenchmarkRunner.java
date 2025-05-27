package com.example.gamedata; // O tu paquete

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit; // Solo si usamos timeout, aunque para búsqueda suele ser rápido

public class SearchBenchmarkRunner {

    private static final String DATASET_FILENAME = "data_generated/games_N1000000.csv"; // El dataset grande
    private static final int NUMBER_OF_SEARCH_RUNS = 10; // Repetir cada búsqueda varias veces para promediar
    private static Random random = new Random();

    // Estructura para resultados: Map<NombreMetodo, Map<TipoAlgoritmo, TiempoPromedioMs>>
    static java.util.Map<String, java.util.Map<String, Double>> searchResults = new java.util.HashMap<>();

    public static void main(String[] args) {
        System.out.println("Iniciando Benchmarks de Búsqueda...");
        System.out.println("Usando dataset: " + DATASET_FILENAME);
        System.out.println("Número de ejecuciones por prueba de búsqueda para promediar: " + NUMBER_OF_SEARCH_RUNS);

        ArrayList<Game> fullDatasetGames = loadGamesFromCSV(DATASET_FILENAME);

        if (fullDatasetGames == null || fullDatasetGames.isEmpty()) {
            System.err.println("No se pudo cargar el dataset. Abortando benchmarks de búsqueda.");
            return;
        }
        System.out.println("Dataset cargado con " + fullDatasetGames.size() + " juegos.");

        // --- 1. Pruebas para getGamesByPrice ---
        benchmarkSearch("getGamesByPrice", fullDatasetGames, dataset -> {
            // Seleccionar un precio aleatorio que EXISTA en el dataset
            Game randomGame = fullDatasetGames.get(random.nextInt(fullDatasetGames.size()));
            int priceToSearch = randomGame.getPrice();
            return dataset.getGamesByPrice(priceToSearch);
        }, "price");


        // --- 2. Pruebas para getGamesByPriceRange ---
        benchmarkSearch("getGamesByPriceRange", fullDatasetGames, dataset -> {
            // Seleccionar un rango de precios aleatorio
            int price1 = fullDatasetGames.get(random.nextInt(fullDatasetGames.size())).getPrice();
            int price2 = fullDatasetGames.get(random.nextInt(fullDatasetGames.size())).getPrice();
            int minPrice = Math.min(price1, price2);
            int maxPrice = Math.max(price1, price2);
            if (minPrice == maxPrice) maxPrice += 1000; // Asegurar un pequeño rango
            return dataset.getGamesByPriceRange(minPrice, maxPrice);
        }, "price");

        // --- 3. Pruebas para getGamesByCategory ---
        benchmarkSearch("getGamesByCategory", fullDatasetGames, dataset -> {
            // Seleccionar una categoría aleatoria que EXISTA
            Game randomGame = fullDatasetGames.get(random.nextInt(fullDatasetGames.size()));
            String categoryToSearch = randomGame.getCategory();
            return dataset.getGamesByCategory(categoryToSearch);
        }, "category");

        // --- 4. Pruebas para getGamesByQuality (si aplica) ---
        benchmarkSearch("getGamesByQuality", fullDatasetGames, dataset -> {
            Game randomGame = fullDatasetGames.get(random.nextInt(fullDatasetGames.size()));
            int qualityToSearch = randomGame.getQuality();
            return dataset.getGamesByQuality(qualityToSearch);
        }, "quality");


        printSearchResultsTable();
        System.out.println("\n--- Benchmarks de Búsqueda Completados ---");
    }

    /**
     * Interfaz funcional para representar la operación de búsqueda a medir.
     */
    @FunctionalInterface
    interface SearchOperation {
        List<Game> search(Dataset dataset);
    }

    /**
     * Método genérico para hacer benchmark de una operación de búsqueda.
     * @param methodName Nombre del método de Dataset que se prueba (ej. "getGamesByPrice")
     * @param baseGames Lista original de juegos (para resetear el dataset)
     * @param operation La operación de búsqueda a ejecutar
     * @param attributeToSortForBinary El atributo por el cual ordenar para la prueba de búsqueda binaria
     */
    private static void benchmarkSearch(String methodName, ArrayList<Game> baseGames,
                                        SearchOperation operation, String attributeToSortForBinary) {
        
        System.out.println("\n--- Benchmarking: " + methodName + " ---");
        searchResults.putIfAbsent(methodName, new java.util.HashMap<>());
        long totalDurationLinearNs = 0;
        long totalDurationBinaryNs = 0;

        // === Búsqueda Lineal ===
        // Asegurarse de que el dataset no esté ordenado por el atributo relevante (o resetear sortedByAttribute)
        Dataset linearDataset = new Dataset(new ArrayList<>(baseGames)); // Copia fresca
        // Para forzar lineal, podríamos internamente en Dataset tener un método para resetear sortedByAttribute
        // o pasar un atributo de ordenamiento que no sea 'attributeToSortForBinary'.
        // Por ahora, confiamos en que un Dataset nuevo no está ordenado, o que los métodos de búsqueda
        // en Dataset chequean `sortedByAttribute` correctamente.
        // Una forma explícita sería: linearDataset.forceAttributeSort(null) si tuviéramos ese método.
        // O, si 'name' no es un atributo por el que ordenamos para búsqueda:
        // linearDataset.sortByAlgorithm("collections.sort", "name", false); 
        // La forma más simple es que el constructor de Dataset ponga sortedByAttribute a null.

        System.out.print("  Lineal: ");
        for (int i = 0; i < NUMBER_OF_SEARCH_RUNS; i++) {
            // Crear una copia para asegurar que el estado no se altera entre búsquedas si la op lo hiciera
            Dataset currentLinearDataset = new Dataset(new ArrayList<>(baseGames)); 
            // Podríamos asegurarnos que no está ordenado por el atributo:
            if (attributeToSortForBinary.equals(currentLinearDataset.getSortedByAttribute())) {
                currentLinearDataset.sortByAlgorithm("collections.sort", "name", false); // Ordenar por otro atributo para "desordenar"
            }


            long startTime = System.nanoTime();
            List<Game> result = operation.search(currentLinearDataset);
            long endTime = System.nanoTime();
            totalDurationLinearNs += (endTime - startTime);
            // System.out.println("    Run " + (i+1) + " lineal: " + (endTime - startTime) + " ns, encontrados: " + result.size());
        }
        double avgLinearMs = (double) totalDurationLinearNs / NUMBER_OF_SEARCH_RUNS / 1_000_000.0;
        System.out.printf("Tiempo Promedio: %.6f ms%n", avgLinearMs);
        searchResults.get(methodName).put("linearSearch", avgLinearMs);

        // === Búsqueda Binaria ===
        // Primero, ordenar el dataset por el atributo relevante
        Dataset binaryDataset = new Dataset(new ArrayList<>(baseGames)); // Copia fresca
        // System.out.print("  Ordenando para binaria por " + attributeToSortForBinary + "... ");
        // long sortStartTime = System.nanoTime();
        binaryDataset.sortByAlgorithm("collections.sort", attributeToSortForBinary, false); // Usar el más rápido
        // long sortEndTime = System.nanoTime();
        // System.out.println("hecho en " + (sortEndTime - sortStartTime)/1_000_000.0 + " ms.");

        System.out.print("  Binaria: ");
        for (int i = 0; i < NUMBER_OF_SEARCH_RUNS; i++) {
             // No es necesario recrear binaryDataset aquí si la operación de búsqueda no modifica el dataset
            long startTime = System.nanoTime();
            List<Game> result = operation.search(binaryDataset); // binaryDataset YA está ordenado
            long endTime = System.nanoTime();
            totalDurationBinaryNs += (endTime - startTime);
            // System.out.println("    Run " + (i+1) + " binaria: " + (endTime - startTime) + " ns, encontrados: " + result.size());
        }
        double avgBinaryMs = (double) totalDurationBinaryNs / NUMBER_OF_SEARCH_RUNS / 1_000_000.0;
        System.out.printf("Tiempo Promedio: %.6f ms%n", avgBinaryMs);
        searchResults.get(methodName).put("binarySearch", avgBinaryMs);
    }


    public static ArrayList<Game> loadGamesFromCSV(String filename) {
        ArrayList<Game> games = new ArrayList<>();
        String line = "";
        String cvsSplitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; // Regex para CSV que maneja comas dentro de comillas

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Saltar encabezado

            while ((line = br.readLine()) != null) {
                String[] gameData = line.split(cvsSplitBy, -1); // -1 para no descartar campos vacíos al final
                if (gameData.length >= 4) {
                    try {
                        String name = unescapeCsvValue(gameData[0]);
                        String category = unescapeCsvValue(gameData[1]);
                        int price = Integer.parseInt(gameData[2].trim());
                        int quality = Integer.parseInt(gameData[3].trim());
                        games.add(new Game(name, category, price, quality));
                    } catch (NumberFormatException e) {
                        System.err.println("Error parseando número en línea CSV: " + line + " -> " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                         System.err.println("Línea CSV con formato incorrecto (menos de 4 campos): " + line);
                    }
                } else {
                     System.err.println("Línea CSV con formato incorrecto (detectados " + gameData.length + " campos): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return null; // O retornar lista vacía y manejarlo arriba
        }
        return games;
    }
    
    private static String unescapeCsvValue(String value) {
        if (value == null) return "";
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1); // Quitar comillas externas
            value = value.replace("\"\"", "\""); // Reemplazar comillas dobles escapadas
        }
        return value;
    }

    private static void printSearchResultsTable() {
        System.out.println("\n\nCuadro 2: Tiempos de ejecución de búsqueda");
        System.out.println("---------------------------------------------------------------------");
        System.out.printf("| %-25s | %-15s | %-20s |\n", "Método", "Algoritmo", "Tiempo (milisegundos)");
        System.out.println("---------------------------------------------------------------------");

        // Orden específico de los métodos para la tabla
        String[] methodOrder = {"getGamesByPrice", "getGamesByPriceRange", "getGamesByCategory", "getGamesByQuality"};

        for (String methodName : methodOrder) {
            if (searchResults.containsKey(methodName)) {
                java.util.Map<String, Double> methodTimes = searchResults.get(methodName);
                
                // Búsqueda Lineal
                if (methodTimes.containsKey("linearSearch")) {
                    System.out.printf("| %-25s | %-15s | %-20.6f |\n", methodName, "linearSearch", methodTimes.get("linearSearch"));
                }
                // Búsqueda Binaria
                if (methodTimes.containsKey("binarySearch")) {
                     System.out.printf("| %-25s | %-15s | %-20.6f |\n", methodName, "binarySearch", methodTimes.get("binarySearch"));
                }
            }
        }
        System.out.println("---------------------------------------------------------------------");
    }
}