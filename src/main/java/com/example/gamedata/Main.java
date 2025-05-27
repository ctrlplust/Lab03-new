package com.example.gamedata;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<Game> initialGames = new ArrayList<>(Arrays.asList(
                new Game("The Witcher 3", "RPG", 25000, 95),
                new Game("Cyberpunk 2077", "RPG", 45000, 70),
                new Game("Red Dead Redemption 2", "Accion-Aventura", 30000, 98),
                new Game("Doom Eternal", "FPS", 20000, 90),
                new Game("Zelda: BOTW", "Accion-Aventura", 40000, 97),
                new Game("Stardew Valley", "Simulacion", 10000, 96),
                new Game("Dark Souls 3", "Accion-RPG", 20000, 89),
                new Game("Hades", "Roguelike", 15000, 93),
                new Game("Celeste", "Platformero", 12000, 92),
                new Game("Hollow Knight", "Metroidvania", 10000, 90)
        ));

        Dataset gameDataset = new Dataset(initialGames);

        System.out.println("--- Estado Inicial (" + gameDataset.getSortedByAttribute() + ") ");
        gameDataset.getData().forEach(System.out::println);

        System.out.println(" Ordenando por Precio usando BubbleSort ");
        gameDataset.sortByAlgorithm("bubbleSort", "price");
        gameDataset.getData().forEach(System.out::println);
        System.out.println("Ordenado por: " + gameDataset.getSortedByAttribute());


        System.out.println(" Juegos con precio 20000 (Búsqueda Binaria) ");
        ArrayList<Game> gamesAt20000 = gameDataset.getGamesByPrice(20000);
        gamesAt20000.forEach(System.out::println);

        System.out.println("Juegos con precio entre 10000 y 15000 (Búsqueda Binaria) ");
        ArrayList<Game> gamesInPriceRange = gameDataset.getGamesByPriceRange(10000, 15000);
        gamesInPriceRange.forEach(System.out::println);
        
        System.out.println(" Juegos con precio entre 50000 y 60000 (Búsqueda Binaria) ");
        ArrayList<Game> emptyRange = gameDataset.getGamesByPriceRange(50000, 60000);
        System.out.println("Juegos encontrados: " + emptyRange.size());
        emptyRange.forEach(System.out::println);


        System.out.println(" Ordenando por Categoría usando Collections.sort ");
        // "collections.sort" es un ejemplo, podría ser cualquier cosa no reconocida
        gameDataset.sortByAlgorithm("collections.sort", "category"); 
        gameDataset.getData().forEach(System.out::println);
        System.out.println("Ordenado por: " + gameDataset.getSortedByAttribute());

        System.out.println(" Juegos de categoría RPG (Búsqueda Binaria) ");
        ArrayList<Game> rpgGames = gameDataset.getGamesByCategory("RPG");
        rpgGames.forEach(System.out::println);

        System.out.println(" Ordenando por Calidad usando QuickSort ");
        gameDataset.sortByAlgorithm("quickSort", "quality");
        gameDataset.getData().forEach(System.out::println);
        System.out.println("Ordenado por: " + gameDataset.getSortedByAttribute());
        
        System.out.println(" Juegos con calidad 90 (Búsqueda Binaria) ");
        ArrayList<Game> quality90Games = gameDataset.getGamesByQuality(90);
        quality90Games.forEach(System.out::println);

        System.out.println(" Volviendo a desordenar (simulado) y búsqueda lineal ");
        // Para simular desorden, creamos un nuevo dataset o modificamos y reseteamos sortedByAttribute
        Dataset gameDatasetUnsorted = new Dataset(new ArrayList<>(initialGames)); // Copia fresca
        System.out.println("Dataset desordenado. Buscando juegos de categoría 'FPS' (Lineal):");
        ArrayList<Game> fpsGamesLinear = gameDatasetUnsorted.getGamesByCategory("FPS");
        fpsGamesLinear.forEach(System.out::println);
        System.out.println("Ordenado por (debería ser null o similar): " + gameDatasetUnsorted.getSortedByAttribute());
    }
}