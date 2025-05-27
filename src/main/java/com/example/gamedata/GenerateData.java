package com.example.gamedata;
import com.example.gamedata.Game;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerateData {

    private static final String[] NAME_PART_1 = {
            "Galaxy", "Shadow", "Dragon", "Cyber", "War", "Ancient", "Lost", "Eternal", "Star", "Time",
            "Crystal", "Iron", "Solar", "Void", "Mystic", "Frozen", "Hero", "Legend", "Cosmic", "Final"
    };

    private static final String[] NAME_PART_2 = {
            "Quest", "Blade", "Chronicles", "Runner", "Forge", "Empire", "Saga", "Legacy", "Frontier", "Guardians",
            "Echoes", "Storm", "Heart", "Riders", "Odyssey", "Protocol", "Reign", "Defenders", "Horizon", "Pact"
    };

    private static final String[] CATEGORIES = {
            "Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación",
            "Puzzle", "Carreras", "Lucha", "Musical", "Terror", "Plataformas"
    };

    private static final int MAX_PRICE = 70000;
    private static final int MIN_PRICE = 0;
    private static final int MAX_QUALITY = 100;
    private static final int MIN_QUALITY = 0;

    private static Random random = new Random();

    private static String generateRandomName() {
        String part1 = NAME_PART_1[random.nextInt(NAME_PART_1.length)];
        String part2 = NAME_PART_2[random.nextInt(NAME_PART_2.length)];
        // Combina las dos partes con un espacio entre ellas
        return part1 + part2;
    }

    private static String generateRandomCategory() {
        return CATEGORIES[random.nextInt(CATEGORIES.length)];
    }

    private static int generateRandomPrice() {
        return random.nextInt(MAX_PRICE - MIN_PRICE + 1) + MIN_PRICE;
    }

    private static int generateRandomQuality() {
        return random.nextInt(MAX_QUALITY - MIN_QUALITY + 1) + MIN_QUALITY;
    }

    /**
     * @param N El número de juegos a generar.
     * @return Una ArrayList de objetos Game generados aleatoriamente.
     */
    public static ArrayList<Game> generateRandomGames(int N) {
        if (N <= 0) {
            return new ArrayList<>();
        }
        ArrayList<Game> games = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            String name = generateRandomName();
            String category = generateRandomCategory();
            int price = generateRandomPrice();
            int quality = generateRandomQuality();
            games.add(new Game(name, category, price, quality));
        }
        return games;
    }

    /**
     * @param gamesList La lista de juegos a guardar.
     * @param filename El nombre del archivo donde se guardarán los datos (ej. "games_100.csv").
     */
    public static void saveGamesToCSV(ArrayList<Game> gamesList, String filename) {
        // Crear un directorio 'data' si no existe, para organizar los archivos generados
        java.io.File dataDir = new java.io.File("data_generated");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        String filePath = dataDir.getPath() + java.io.File.separator + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Escribir encabezado (opcional, pero útil para CSV)
            writer.write("Name,Category,Price,Quality\n");

            for (Game game : gamesList) {
                // Escapar valores para CSV y unirlos con comas
                String line = String.join(",",
                        escapeCsvValue(game.getName()),
                        escapeCsvValue(game.getCategory()),
                        String.valueOf(game.getPrice()),
                        String.valueOf(game.getQuality())
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Datos guardados exitosamente en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos en el archivo " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * @param value el String a escapar.
     * @return el String escapado para CSV.
     */
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        // Si el valor contiene comas, comillas dobles o saltos de línea, debe ir entre comillas dobles.
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Reemplazar comillas dobles internas con dos comillas dobles.
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }


    /**
     * Método main para la clase GenerateData.
     * Su propósito principal aquí es generar y guardar los conjuntos de datos solicitados.
     */
    public static void main(String[] args) {
        System.out.println("--- Tarea: Generar y Guardar Conjuntos de Datos ---");

        // Tamaños de los datasets a generar
        int[] datasetSizes = {
            100,      // 10^2
            10000,    // 10^4
            1000000   // 10^6 (¡Este puede tardar un poco en generar y guardar!)
        };

        for (int size : datasetSizes) {
            System.out.println("\nGenerando dataset de tamaño: " + size);
            long startTime = System.nanoTime();
            ArrayList<Game> games = generateRandomGames(size);
            long endTime = System.nanoTime();
            System.out.println("Generación completada en: " + (endTime - startTime) / 1_000_000 + " ms.");

            if (!games.isEmpty()) {
                String filename = "games_N" + size + ".csv";
                System.out.println("Guardando dataset en " + filename + "...");
                startTime = System.nanoTime();
                saveGamesToCSV(games, filename);
                endTime = System.nanoTime();
                System.out.println("Guardado completado en: " + (endTime - startTime) / 1_000_000 + " ms.");

            } else {
                System.err.println("No se generaron juegos para el tamaño " + size + ". No se guardará el archivo.");
            }
        }

        System.out.println("\n--- Proceso de Generación y Guardado Completado ---");

        // Pequeña prueba adicional del método generateRandomGames para mostrar algunos datos (opcional)
        System.out.println("\n--- Ejemplo de 5 juegos generados (no guardados aquí) ---");
        ArrayList<Game> sampleGames = generateRandomGames(5);
        if (!sampleGames.isEmpty()) {
            for (Game game : sampleGames) {
                System.out.println(game);
            }
        } else {
            System.out.println("No se pudieron generar juegos de muestra.");
        }
    }
}