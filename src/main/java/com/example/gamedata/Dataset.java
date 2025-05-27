package com.example.gamedata; // Asegúrate de usar tu estructura de paquete

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dataset {
    private ArrayList<Game> data;
    private String sortedByAttribute; // "price", "category", "quality", o null/"" si no está ordenado o desconocido

    // Comparators
    private static final Comparator<Game> PRICE_COMPARATOR = Comparator.comparingInt(Game::getPrice);
    private static final Comparator<Game> CATEGORY_COMPARATOR = Comparator.comparing(Game::getCategory);
    private static final Comparator<Game> QUALITY_COMPARATOR = Comparator.comparingInt(Game::getQuality);

    public Dataset(ArrayList<Game> data) {
        this.data = (data != null) ? new ArrayList<>(data) : new ArrayList<>(); // Copia defensiva o lista nueva
        this.sortedByAttribute = null; // Inicialmente no está ordenado o el orden es desconocido
    }

    public ArrayList<Game> getData() {
        return new ArrayList<>(data); // Devuelve una copia para evitar modificaciones externas no controladas
    }

    public String getSortedByAttribute() {
        return sortedByAttribute;
    }

    // --- MÉTODOS DE BÚSQUEDA ---

    public ArrayList<Game> getGamesByPrice(int price) {
        if ("price".equals(this.sortedByAttribute)) {
            // Búsqueda Binaria para precio exacto (puede haber múltiples juegos con el mismo precio)
            return binarySearchForExactPrice(price);
        } else {
            // Búsqueda Lineal
            return linearSearchByPrice(price);
        }
    }

    private ArrayList<Game> linearSearchByPrice(int price) {
        ArrayList<Game> result = new ArrayList<>();
        for (Game game : this.data) {
            if (game.getPrice() == price) {
                result.add(game);
            }
        }
        return result;
    }

    private ArrayList<Game> binarySearchForExactPrice(int targetPrice) {
        ArrayList<Game> result = new ArrayList<>();
        // Usamos un juego "dummy" para la búsqueda con el comparador de precio
        Game dummyKeyGame = new Game("", "", targetPrice, 0); // Solo el precio importa para el comparador
        int index = Collections.binarySearch(this.data, dummyKeyGame, PRICE_COMPARATOR);

        if (index >= 0) {
            // Elemento encontrado, ahora busca duplicados a la izquierda y derecha
            result.add(this.data.get(index));

            // Buscar a la izquierda
            int i = index - 1;
            while (i >= 0 && this.data.get(i).getPrice() == targetPrice) {
                result.add(this.data.get(i));
                i--;
            }
            // Buscar a la derecha
            i = index + 1;
            while (i < this.data.size() && this.data.get(i).getPrice() == targetPrice) {
                result.add(this.data.get(i));
                i++;
            }
        }
        // Opcional: Si el orden de los resultados importa, ordenarlos aquí (ej. por nombre)
        // Collections.sort(result, Comparator.comparing(Game::getName));
        return result;
    }


    public ArrayList<Game> getGamesByPriceRange(int lowerPrice, int higherPrice) {
        if ("price".equals(this.sortedByAttribute)) {
            // Variación de Búsqueda Binaria para rango
            return binarySearchForPriceRange(lowerPrice, higherPrice);
        } else {
            // Búsqueda Lineal
            return linearSearchByPriceRange(lowerPrice, higherPrice);
        }
    }

    private ArrayList<Game> linearSearchByPriceRange(int lowerPrice, int higherPrice) {
        ArrayList<Game> result = new ArrayList<>();
        for (Game game : this.data) {
            if (game.getPrice() >= lowerPrice && game.getPrice() <= higherPrice) {
                result.add(game);
            }
        }
        return result;
    }

    private ArrayList<Game> binarySearchForPriceRange(int lowerPrice, int higherPrice) {
        // Encontrar el índice del primer elemento >= lowerPrice
        int startIndex = findFirstIndexGreaterThanOrEqual(this.data, lowerPrice, PRICE_COMPARATOR);
        if (startIndex == -1) { // No hay juegos en el rango
            return new ArrayList<>();
        }

        // Encontrar el índice del último elemento <= higherPrice
        // Empezamos la búsqueda para el límite superior desde donde encontramos el inferior
        // Esto es una optimización, pero podría hacerse sobre toda la lista también.
        // Para simplificar y robustecer, buscaremos en toda la sublista relevante o en toda la lista.
        int endIndex = findLastIndexLessThanOrEqual(this.data, higherPrice, PRICE_COMPARATOR);

        if (endIndex == -1 || startIndex > endIndex || this.data.get(startIndex).getPrice() > higherPrice) {
             return new ArrayList<>(); // No hay juegos en el rango
        }
        
        // Asegurarse que los juegos en los bordes realmente cumplen
        if (this.data.get(endIndex).getPrice() < lowerPrice) {
            return new ArrayList<>();
        }

        ArrayList<Game> result = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            // Doble chequeo por si los métodos findFirst/findLast no son perfectos
            // o si hay elementos fuera del rango entre los índices encontrados (no debería pasar si están bien implementados)
             Game currentGame = this.data.get(i);
             if (currentGame.getPrice() >= lowerPrice && currentGame.getPrice() <= higherPrice) {
                result.add(currentGame);
             }
        }
        return result;
        // Alternativa más simple si findFirst y findLast son precisos:
        // return new ArrayList<>(this.data.subList(startIndex, endIndex + 1));
        // Hay que tener cuidado con subList, ya que el segundo índice es exclusivo.
        // Y asegurar que startIndex y endIndex realmente delimitan el rango.
    }

    // Helper para binarySearchForPriceRange: encuentra el primer índice cuyo valor es >= key
    private int findFirstIndexGreaterThanOrEqual(List<Game> list, int keyPrice, Comparator<Game> comparator) {
        int low = 0;
        int high = list.size() - 1;
        int ans = -1;
        Game keyGame = new Game("", "", keyPrice, 0); // Dummy game for comparison

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(list.get(mid), keyGame) >= 0) { // list.get(mid).getPrice() >= keyPrice
                ans = mid;
                high = mid - 1; // Intentar encontrar uno más a la izquierda
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

    // Helper para binarySearchForPriceRange: encuentra el último índice cuyo valor es <= key
    private int findLastIndexLessThanOrEqual(List<Game> list, int keyPrice, Comparator<Game> comparator) {
        int low = 0;
        int high = list.size() - 1;
        int ans = -1;
        Game keyGame = new Game("", "", keyPrice, 0); // Dummy game for comparison

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(list.get(mid), keyGame) <= 0) { // list.get(mid).getPrice() <= keyPrice
                ans = mid;
                low = mid + 1; // Intentar encontrar uno más a la derecha
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }


    public ArrayList<Game> getGamesByCategory(String category) {
        if ("category".equals(this.sortedByAttribute)) {
            return binarySearchForExactAttribute(category, CATEGORY_COMPARATOR, game -> game.getCategory().equals(category));
        } else {
            return linearSearchByAttribute(game -> game.getCategory().equalsIgnoreCase(category)); // O .equals si es case-sensitive
        }
    }

    public ArrayList<Game> getGamesByQuality(int quality) {
        if ("quality".equals(this.sortedByAttribute)) {
             return binarySearchForExactAttribute(quality, QUALITY_COMPARATOR, game -> game.getQuality() == quality);
        } else {
            return linearSearchByAttribute(game -> game.getQuality() == quality);
        }
    }
    
    // Helper genérico para búsqueda lineal por un predicado
    private ArrayList<Game> linearSearchByAttribute(java.util.function.Predicate<Game> predicate) {
        ArrayList<Game> result = new ArrayList<>();
        for (Game game : this.data) {
            if (predicate.test(game)) {
                result.add(game);
            }
        }
        return result;
    }

    // Helper genérico para búsqueda binaria de un atributo exacto (con múltiples ocurrencias)
    private <T> ArrayList<Game> binarySearchForExactAttribute(T targetValue, Comparator<Game> comparator, java.util.function.Predicate<Game> exactMatchPredicate) {
        ArrayList<Game> result = new ArrayList<>();
        // Crear un Game "dummy" para Collections.binarySearch no es tan directo para String/int genéricos.
        // En su lugar, podemos implementar una búsqueda binaria que encuentre el *primer* match,
        // luego expandir. O usar Collections.binarySearch con un objeto clave adecuado.
        // Para simplificar, dado que ya tenemos los comparadores:
        // Haremos una búsqueda binaria para encontrar *un* índice, luego expandiremos.

        // Creamos un game "key" para la búsqueda. Esto es un poco un hack.
        // Se necesitaría un constructor o un game específico para cada tipo de búsqueda.
        // Para categoría:
        Game keyGame;
        if (targetValue instanceof String) {
            keyGame = new Game("", (String)targetValue, 0,0);
        } else if (targetValue instanceof Integer && comparator == QUALITY_COMPARATOR) { // Asumimos que es quality
            keyGame = new Game("", "", 0, (Integer)targetValue);
        } else { // Debería ser price, pero ese tiene su propio método. Esto es para generalizar.
            // Esta parte necesitaría más refinamiento si se usa para otros atributos numéricos.
            // Por ahora, los métodos específicos para precio, categoría y calidad son más robustos.
            // Este método genérico es más conceptual aquí.
            System.err.println("Advertencia: binarySearchForExactAttribute usado con tipo no esperado o comparador no coincidente.");
            return linearSearchByAttribute(exactMatchPredicate); // fallback
        }


        int index = Collections.binarySearch(this.data, keyGame, comparator);

        if (index >= 0) {
            // Elemento encontrado, ahora busca duplicados a la izquierda y derecha
            // que cumplan el predicado exacto (importante por si el comparador no es suficiente para igualdad exacta)
            if (exactMatchPredicate.test(this.data.get(index))) {
                 result.add(this.data.get(index));
            }

            // Buscar a la izquierda
            int i = index - 1;
            while (i >= 0 && comparator.compare(this.data.get(i), keyGame) == 0) {
                if (exactMatchPredicate.test(this.data.get(i))) {
                    result.add(this.data.get(i));
                }
                i--;
            }
            // Buscar a la derecha
            i = index + 1;
            while (i < this.data.size() && comparator.compare(this.data.get(i), keyGame) == 0) {
                 if (exactMatchPredicate.test(this.data.get(i))) {
                    result.add(this.data.get(i));
                }
                i++;
            }
        }
        return result;
    }


    // --- MÉTODO DE ORDENAMIENTO ---

    public void sortByAlgorithm(String algorithm, String attribute) {
        Comparator<Game> selectedComparator;
        String effectiveAttribute = (attribute != null) ? attribute.toLowerCase() : "price"; // Default a price

        switch (effectiveAttribute) {
            case "category":
                selectedComparator = CATEGORY_COMPARATOR;
                break;
            case "quality":
                selectedComparator = QUALITY_COMPARATOR;
                break;
            case "price":
            default: // Por defecto o si el atributo no se reconoce, ordenar por precio
                selectedComparator = PRICE_COMPARATOR;
                effectiveAttribute = "price"; // Asegurar que se setea el atributo correcto
                break;
        }

        long startTime = System.nanoTime(); // Para medir tiempo si se desea

        String algoLower = (algorithm != null) ? algorithm.toLowerCase() : "collections.sort";

        switch (algoLower) {
            case "bubblesort":
                bubbleSort(this.data, selectedComparator);
                break;
            case "insertionsort":
                insertionSort(this.data, selectedComparator);
                break;
            case "selectionsort":
                selectionSort(this.data, selectedComparator);
                break;
            case "mergesort":
                mergeSort(this.data, selectedComparator);
                break;
            case "quicksort":
                quickSort(this.data, 0, this.data.size() - 1, selectedComparator);
                break;
            default: // Incluye "collections.sort" o cualquier otro no reconocido
                Collections.sort(this.data, selectedComparator);
                break;
        }
        this.sortedByAttribute = effectiveAttribute; // Actualizar el atributo por el cual está ordenado

        long endTime = System.nanoTime();
        System.out.println("Sorted by " + effectiveAttribute + " using " + algoLower + " in " + (endTime - startTime) + " ns.");
    }

    // --- IMPLEMENTACIONES DE ALGORITMOS DE ORDENAMIENTO ---

    private void bubbleSort(ArrayList<Game> list, Comparator<Game> comparator) {
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break; // Optimización: si no hubo swaps, está ordenado
        }
    }

    private void insertionSort(ArrayList<Game> list, Comparator<Game> comparator) {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            Game key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

    private void selectionSort(ArrayList<Game> list, Comparator<Game> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            Collections.swap(list, i, minIdx);
        }
    }

    // Merge Sort
    private void mergeSort(ArrayList<Game> list, Comparator<Game> comparator) {
        if (list.size() < 2) {
            return;
        }
        int mid = list.size() / 2;
        ArrayList<Game> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<Game> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(list, left, right, comparator);
    }

    private void merge(ArrayList<Game> mainList, ArrayList<Game> left, ArrayList<Game> right, Comparator<Game> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                mainList.set(k++, left.get(i++));
            } else {
                mainList.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            mainList.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            mainList.set(k++, right.get(j++));
        }
    }

    // Quick Sort
    private void quickSort(ArrayList<Game> list, int low, int high, Comparator<Game> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private int partition(ArrayList<Game> list, int low, int high, Comparator<Game> comparator) {
        Game pivot = list.get(high); // Pivote como el último elemento
        int i = (low - 1); // Índice del elemento más pequeño
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}