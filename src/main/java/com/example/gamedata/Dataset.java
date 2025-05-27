package com.example.gamedata; // Asegúrate de usar tu estructura de paquete
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
// ... (otras importaciones que ya tenías)

public class Dataset {
    private ArrayList<Game> data;
    private String sortedByAttribute;

    private static final Comparator<Game> PRICE_COMPARATOR = Comparator.comparingInt(Game::getPrice);
    private static final Comparator<Game> CATEGORY_COMPARATOR = Comparator.comparing(Game::getCategory);
    private static final Comparator<Game> QUALITY_COMPARATOR = Comparator.comparingInt(Game::getQuality);

    public Dataset(ArrayList<Game> data) {
        this.data = (data != null) ? new ArrayList<>(data) : new ArrayList<>();
        this.sortedByAttribute = null;
    }

    public ArrayList<Game> getData() {
        return new ArrayList<>(data);
    }

    public String getSortedByAttribute() {
        return sortedByAttribute;
    }

    // ... (TODOS tus métodos de búsqueda: getGamesByPrice, getGamesByPriceRange, etc.) ...
    // ... (TODOS tus métodos helper de búsqueda: linearSearchByPrice, binarySearchForExactPrice, etc.) ...
    // ... (TODOS tus métodos de ordenamiento: sortByAlgorithm, bubbleSort, insertionSort, etc.) ...
    // PEGA AQUÍ TODO EL CÓDIGO DE LOS MÉTODOS DE DATASET QUE TE PROPORCIONÉ ANTERIORMENTE

    // --- MÉTODOS DE BÚSQUEDA ---

    public ArrayList<Game> getGamesByPrice(int price) {
        if ("price".equals(this.sortedByAttribute)) {
            return binarySearchForExactPrice(price);
        } else {
            System.out.println("Dataset no ordenado por precio. Usando búsqueda lineal para getGamesByPrice.");
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
        Game dummyKeyGame = new Game("", "", targetPrice, 0);
        int index = Collections.binarySearch(this.data, dummyKeyGame, PRICE_COMPARATOR);

        if (index >= 0) {
            result.add(this.data.get(index));
            int i = index - 1;
            while (i >= 0 && this.data.get(i).getPrice() == targetPrice) {
                result.add(this.data.get(i));
                i--;
            }
            i = index + 1;
            while (i < this.data.size() && this.data.get(i).getPrice() == targetPrice) {
                result.add(this.data.get(i));
                i++;
            }
        }
        return result;
    }


    public ArrayList<Game> getGamesByPriceRange(int lowerPrice, int higherPrice) {
        if ("price".equals(this.sortedByAttribute)) {
            return binarySearchForPriceRange(lowerPrice, higherPrice);
        } else {
            System.out.println("Dataset no ordenado por precio. Usando búsqueda lineal para getGamesByPriceRange.");
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
        int startIndex = findFirstIndexGreaterThanOrEqual(this.data, lowerPrice, PRICE_COMPARATOR);
        if (startIndex == -1) {
            return new ArrayList<>();
        }
        int endIndex = findLastIndexLessThanOrEqual(this.data, higherPrice, PRICE_COMPARATOR);
        if (endIndex == -1 || startIndex > endIndex || this.data.get(startIndex).getPrice() > higherPrice) {
             return new ArrayList<>();
        }
        if (this.data.get(endIndex).getPrice() < lowerPrice) {
            return new ArrayList<>();
        }
        ArrayList<Game> result = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
             Game currentGame = this.data.get(i);
             if (currentGame.getPrice() >= lowerPrice && currentGame.getPrice() <= higherPrice) { // Doble chequeo
                result.add(currentGame);
             }
        }
        return result;
    }

    private int findFirstIndexGreaterThanOrEqual(List<Game> list, int keyPrice, Comparator<Game> comparator) {
        int low = 0;
        int high = list.size() - 1;
        int ans = -1;
        Game keyGame = new Game("", "", keyPrice, 0);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(list.get(mid), keyGame) >= 0) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

    private int findLastIndexLessThanOrEqual(List<Game> list, int keyPrice, Comparator<Game> comparator) {
        int low = 0;
        int high = list.size() - 1;
        int ans = -1;
        Game keyGame = new Game("", "", keyPrice, 0);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(list.get(mid), keyGame) <= 0) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return ans;
    }


    public ArrayList<Game> getGamesByCategory(String category) {
        if ("category".equals(this.sortedByAttribute)) {
            return binarySearchForExactAttribute(category, CATEGORY_COMPARATOR, game -> game.getCategory().equalsIgnoreCase(category));
        } else {
            System.out.println("Dataset no ordenado por categoría. Usando búsqueda lineal para getGamesByCategory.");
            return linearSearchByAttribute(game -> game.getCategory().equalsIgnoreCase(category));
        }
    }

    public ArrayList<Game> getGamesByQuality(int quality) {
        if ("quality".equals(this.sortedByAttribute)) {
             return binarySearchForExactAttribute(quality, QUALITY_COMPARATOR, game -> game.getQuality() == quality);
        } else {
            System.out.println("Dataset no ordenado por calidad. Usando búsqueda lineal para getGamesByQuality.");
            return linearSearchByAttribute(game -> game.getQuality() == quality);
        }
    }
    
    private ArrayList<Game> linearSearchByAttribute(java.util.function.Predicate<Game> predicate) {
        ArrayList<Game> result = new ArrayList<>();
        for (Game game : this.data) {
            if (predicate.test(game)) {
                result.add(game);
            }
        }
        return result;
    }

    private <T> ArrayList<Game> binarySearchForExactAttribute(T targetValue, Comparator<Game> comparator, java.util.function.Predicate<Game> exactMatchPredicate) {
        ArrayList<Game> result = new ArrayList<>();
        Game keyGame; // Placeholder
        if (targetValue instanceof String && comparator == CATEGORY_COMPARATOR) {
            keyGame = new Game("", (String)targetValue, 0,0);
        } else if (targetValue instanceof Integer && comparator == QUALITY_COMPARATOR) {
            keyGame = new Game("", "", 0, (Integer)targetValue);
        } else {
             System.err.println("Advertencia: binarySearchForExactAttribute con tipo/comparador no manejado explícitamente. La precisión puede variar.");
             // Fallback a una búsqueda menos precisa o lineal si es necesario.
             // Para este ejemplo, asumiremos que el comparador es el correcto y Collections.binarySearch funcionará.
             // Una implementación más robusta requeriría crear un Game 'clave' específico para cada tipo de T y comparador.
             // Este es un punto donde el código podría necesitar más generalización o especialización.
             // Por ahora, nos apoyamos en que los métodos públicos llaman a este con los comparadores correctos.
             if (targetValue instanceof String) keyGame = new Game("", (String)targetValue, 0,0);
             else if (targetValue instanceof Integer) keyGame = new Game("dummy", "dummy", (Integer)targetValue, (Integer)targetValue); // Podría ser precio o calidad
             else return linearSearchByAttribute(exactMatchPredicate); // No se puede crear un dummy
        }

        int index = Collections.binarySearch(this.data, keyGame, comparator);

        if (index >= 0) {
            if (exactMatchPredicate.test(this.data.get(index))) {
                 result.add(this.data.get(index));
            }
            int i = index - 1;
            while (i >= 0 && comparator.compare(this.data.get(i), keyGame) == 0) {
                if (exactMatchPredicate.test(this.data.get(i))) {
                    result.add(this.data.get(i));
                }
                i--;
            }
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
        String effectiveAttribute = (attribute != null) ? attribute.toLowerCase() : "price";

        switch (effectiveAttribute) {
            case "category":
                selectedComparator = CATEGORY_COMPARATOR;
                break;
            case "quality":
                selectedComparator = QUALITY_COMPARATOR;
                break;
            case "price":
            default:
                selectedComparator = PRICE_COMPARATOR;
                effectiveAttribute = "price";
                break;
        }
        long startTime = System.nanoTime();
        String algoLower = (algorithm != null) ? algorithm.toLowerCase() : "collections.sort";

        // Hacemos una copia para los algoritmos que modifican in-place si no queremos alterar this.data directamente
        // O, como está ahora, this.data se modifica directamente.
        // ArrayList<Game> listToSort = new ArrayList<>(this.data); // Si quisieras trabajar sobre copia

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
            default:
                Collections.sort(this.data, selectedComparator);
                break;
        }
        // this.data = listToSort; // Si trabajaste sobre copia
        this.sortedByAttribute = effectiveAttribute;
        long endTime = System.nanoTime();
        System.out.println("Sorted by " + effectiveAttribute + " using " + algoLower + ". Time: " + (endTime - startTime) + " ns. Size: " + this.data.size());
    }

    // --- IMPLEMENTACIONES DE ALGORITMOS DE ORDENAMIENTO ---
    // (Bubble, Insertion, Selection, Merge, Quick - como los tenías antes)
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
            if (!swapped) break;
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

    private void quickSort(ArrayList<Game> list, int low, int high, Comparator<Game> comparator) {
        if (low < high && low >= 0 && high < list.size()) { // Añadido chequeo de límites
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private int partition(ArrayList<Game> list, int low, int high, Comparator<Game> comparator) {
        Game pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }


    // Método main para probar la clase Dataset
    public static void main(String[] args) {
        System.out.println("--- Probando la Clase Dataset ---");

        // Datos de prueba iniciales
        ArrayList<Game> initialGames = new ArrayList<>(Arrays.asList(
                new Game("Witcher 3", "RPG", 25000, 95),
                new Game("Cyberpunk", "RPG", 45000, 70),
                new Game("RDR2", "Action-Adventure", 30000, 98),
                new Game("Doom", "FPS", 20000, 90),
                new Game("Zelda BOTW", "Action-Adventure", 40000, 97),
                new Game("Stardew", "Simulation", 10000, 96),
                new Game("Dark Souls", "Action-RPG", 20000, 89) // Otro juego con precio 20000
        ));

        Dataset testDataset = new Dataset(new ArrayList<>(initialGames)); // Usar copia para no modificar initialGames
        System.out.println("Dataset inicial (" + testDataset.getSortedByAttribute() + "):");
        testDataset.getData().forEach(System.out::println);

        // 1. Probar getGamesByPrice (lineal y binaria)
        System.out.println("\n--- Pruebas getGamesByPrice ---");
        System.out.println("Buscando precio 20000 (lineal):");
        testDataset.getGamesByPrice(20000).forEach(g -> System.out.println("  " + g));

        testDataset.sortByAlgorithm("bubbleSort", "price");
        System.out.println("\nDataset ordenado por precio (" + testDataset.getSortedByAttribute() + ")");
        testDataset.getData().forEach(System.out::println);
        System.out.println("Buscando precio 20000 (binaria):");
        testDataset.getGamesByPrice(20000).forEach(g -> System.out.println("  " + g));
        System.out.println("Buscando precio 99999 (no existente, binaria): " + testDataset.getGamesByPrice(99999).size() + " encontrados.");

        // 2. Probar getGamesByPriceRange (lineal y binaria)
        System.out.println("\n--- Pruebas getGamesByPriceRange ---");
        Dataset rangeTestDataset = new Dataset(new ArrayList<>(initialGames)); // Reset
        System.out.println("Buscando rango 10000-25000 (lineal):");
        rangeTestDataset.getGamesByPriceRange(10000, 25000).forEach(g -> System.out.println("  " + g));

        rangeTestDataset.sortByAlgorithm("insertionSort", "price");
        System.out.println("\nDataset ordenado por precio (" + rangeTestDataset.getSortedByAttribute() + ")");
        System.out.println("Buscando rango 10000-25000 (binaria):");
        rangeTestDataset.getGamesByPriceRange(10000, 25000).forEach(g -> System.out.println("  " + g));
        System.out.println("Buscando rango 60000-70000 (no existente, binaria): " + rangeTestDataset.getGamesByPriceRange(60000, 70000).size() + " encontrados.");


        // 3. Probar getGamesByCategory (lineal y binaria)
        System.out.println("\n--- Pruebas getGamesByCategory ---");
        Dataset categoryTestDataset = new Dataset(new ArrayList<>(initialGames)); // Reset
        System.out.println("Buscando categoría 'RPG' (lineal):");
        categoryTestDataset.getGamesByCategory("RPG").forEach(g -> System.out.println("  " + g));

        categoryTestDataset.sortByAlgorithm("selectionSort", "category");
        System.out.println("\nDataset ordenado por categoría (" + categoryTestDataset.getSortedByAttribute() + ")");
        categoryTestDataset.getData().forEach(System.out::println);
        System.out.println("Buscando categoría 'RPG' (binaria):");
        categoryTestDataset.getGamesByCategory("RPG").forEach(g -> System.out.println("  " + g));
        System.out.println("Buscando categoría 'Puzzle' (no existente, binaria): " + categoryTestDataset.getGamesByCategory("Puzzle").size() + " encontrados.");

        // 4. Probar getGamesByQuality (lineal y binaria)
        System.out.println("\n--- Pruebas getGamesByQuality ---");
        Dataset qualityTestDataset = new Dataset(new ArrayList<>(initialGames)); // Reset
        System.out.println("Buscando calidad 90 (lineal):");
        qualityTestDataset.getGamesByQuality(90).forEach(g -> System.out.println("  " + g));
        
        qualityTestDataset.sortByAlgorithm("mergeSort", "quality");
        System.out.println("\nDataset ordenado por calidad (" + qualityTestDataset.getSortedByAttribute() + ")");
        qualityTestDataset.getData().forEach(System.out::println);
        System.out.println("Buscando calidad 90 (binaria):");
        qualityTestDataset.getGamesByQuality(90).forEach(g -> System.out.println("  " + g));
        System.out.println("Buscando calidad 50 (no existente, binaria): " + qualityTestDataset.getGamesByQuality(50).size() + " encontrados.");

        // 5. Probar sortByAlgorithm con diferentes algoritmos y atributos
        System.out.println("\n--- Pruebas sortByAlgorithm ---");
        Dataset sortTestDataset = new Dataset(new ArrayList<>(initialGames));
        System.out.println("Original:");
        sortTestDataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - " + g.getPrice()));
        
        sortTestDataset.sortByAlgorithm("quickSort", "price");
        System.out.println("\nOrdenado por precio (quickSort):");
        sortTestDataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - " + g.getPrice()));

        sortTestDataset.sortByAlgorithm("bubbleSort", "name"); // 'name' no es un atributo soportado, debería default a 'price' o manejarse
                                                              // Lo actualicé para que default a 'price', pero para prueba vamos a usar 'category'
        sortTestDataset.sortByAlgorithm("bubbleSort", "category");
        System.out.println("\nOrdenado por categoria (bubbleSort):");
        sortTestDataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - " + g.getCategory()));

        sortTestDataset.sortByAlgorithm("unknownAlgorithm", "quality"); // Debería usar Collections.sort()
        System.out.println("\nOrdenado por calidad (Collections.sort - fallback):");
        sortTestDataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - " + g.getQuality()));
        
        sortTestDataset.sortByAlgorithm("insertionSort", "unknownAttribute"); // Debería usar price por defecto
        System.out.println("\nOrdenado por atributo desconocido (debería ser price, insertionSort):");
        sortTestDataset.getData().forEach(g -> System.out.println("  " + g.getName() + " - " + g.getPrice()));


        System.out.println("\n--- Fin de Pruebas Dataset ---");
    }
}