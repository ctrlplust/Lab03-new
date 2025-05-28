package com.example.gamedata;

import java.util.Objects;

public class Game {
    private String name;
    private String category;
    private int price; // Precio en pesos chilenos
    private int quality; // Entre 0 y 100

    public Game(String name, String category, int price, int quality) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quality = quality;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getQuality() {
        return quality;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "Game{" +
               "name='" + name + '\'' +
               ", category='" + category + '\'' +
               ", price=" + price +
               ", quality=" + quality +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return price == game.price &&
               quality == game.quality &&
               Objects.equals(name, game.name) &&
               Objects.equals(category, game.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, price, quality);
    }

    // ***** MÉTODO MAIN PARA PROBAR LA CLASE GAME *****
    public static void main(String[] args) {
        System.out.println("--- Probando la Clase Game ---");

        // 1. Probar el constructor y getters
        Game game1 = new Game("Epic Adventure", "Aventura", 29990, 85);
        System.out.println("Juego 1 (creado): " + game1.toString());
        System.out.println("Nombre: " + game1.getName());
        System.out.println("Categoría: " + game1.getCategory());
        System.out.println("Precio: " + game1.getPrice());
        System.out.println("Calidad: " + game1.getQuality());

        // 2. Probar los setters
        System.out.println("\nModificando Juego 1 con setters...");
        game1.setName("Super Epic Adventure");
        game1.setPrice(35000);
        game1.setQuality(90);
        System.out.println("Juego 1 (modificado): " + game1);

        // 3. Probar el método equals y hashCode
        Game game2 = new Game("Super Epic Adventure", "Aventura", 35000, 90);
        Game game3 = new Game("Different Game", "RPG", 45000, 70);

        System.out.println("\nComparando game1 y game2 (deberían ser iguales): " + game1.equals(game2));
        System.out.println("Comparando game1 y game3 (deberían ser diferentes): " + game1.equals(game3));

        System.out.println("HashCode game1: " + game1.hashCode());
        System.out.println("HashCode game2: " + game2.hashCode()); // Debería ser igual al de game1
        System.out.println("HashCode game3: " + game3.hashCode());

        System.out.println("\n--- Fin de Pruebas Game ---");
    }
}