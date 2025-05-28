package com.example.gamedata;

import java.util.ArrayList;
import java.util.Collections;

public class DatasetCountingSort {
    private ArrayList<Game> data;
    private static final int MIN_QUALITY_VALUE = 0;
    private static final int MAX_QUALITY_VALUE = 100;

    public DatasetCountingSort(ArrayList<Game> data) {
        this.data = (data != null) ? new ArrayList<>(data) : new ArrayList<>();
    }

    public ArrayList<Game> getData() {
        return new ArrayList<>(data);
    }

    public void countingSortByQuality() {
        if (data == null || data.isEmpty()) {
            return;
        }

        int n = data.size();
        int range = MAX_QUALITY_VALUE - MIN_QUALITY_VALUE + 1;

        int[] count = new int[range];

        for (Game game : data) {
            count[game.getQuality() - MIN_QUALITY_VALUE]++;
        }

        // Acumular los conteos
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        ArrayList<Game> output = new ArrayList<>(Collections.nCopies(n, null));
        for (int i = n - 1; i >= 0; i--) {
            Game currentGame = data.get(i);
            int qualityIndex = currentGame.getQuality() - MIN_QUALITY_VALUE;
            output.set(count[qualityIndex] - 1, currentGame);
            count[qualityIndex]--;
        }

        // Copiar el resultado a data
        for (int i = 0; i < n; i++) {
            data.set(i, output.get(i));
        }
    }

    // Método de prueba
    public static void main(String[] args) {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game("Game A", "RPG", 20000, 95));
        games.add(new Game("Game B", "Accion", 30000, 70));
        games.add(new Game("Game C", "Estrategia", 15000, 95));
        games.add(new Game("Game D", "Simulacion", 10000, 80));
        games.add(new Game("Game E", "RPG", 50000, 70));

        DatasetCountingSort dataset = new DatasetCountingSort(games);

        System.out.println("Antes de Counting Sort:");
        dataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - Quality: " + g.getQuality()));

        dataset.countingSortByQuality();

        System.out.println("\nDespués de Counting Sort:");
        dataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - Quality: " + g.getQuality()));
    }
}