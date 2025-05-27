package com.example.gamedata; // Asegúrate de usar tu estructura de paquete

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

    // Setters (opcionales, pero pueden ser útiles)
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

    // hashCode y equals son importantes si vas a usar juegos en colecciones como HashSets/HashMaps
    // o para comparaciones más robustas en algunos casos.
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
}