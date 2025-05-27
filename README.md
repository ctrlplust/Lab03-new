# Lab03-new
javac -d bin src/main/java/com/example/gamedata/*.java
java -cp bin com.example.gamedata.Main
--- Estado Inicial (null) ---
Game{name='The Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Cyberpunk 2077', category='RPG', price=45000, quality=70}
Game{name='Red Dead Redemption 2', category='Action-Adventure', price=30000, quality=98}
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Game{name='Zelda: BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Stardew Valley', category='Simulation', price=10000, quality=96}
Game{name='Dark Souls 3', category='Action-RPG', price=20000, quality=89}
Game{name='Hades', category='Roguelike', price=15000, quality=93}
Game{name='Celeste', category='Platformer', price=12000, quality=92}
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}

--- Ordenando por Precio usando BubbleSort ---
Sorted by price using bubblesort. Time: 98434 ns. Size: 10
Game{name='Stardew Valley', category='Simulation', price=10000, quality=96}
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}
Game{name='Celeste', category='Platformer', price=12000, quality=92}
Game{name='Hades', category='Roguelike', price=15000, quality=93}
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Game{name='Dark Souls 3', category='Action-RPG', price=20000, quality=89}
Game{name='The Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Red Dead Redemption 2', category='Action-Adventure', price=30000, quality=98}
Game{name='Zelda: BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Cyberpunk 2077', category='RPG', price=45000, quality=70}
Ordenado por: price

--- Juegos con precio 20000 (Búsqueda Binaria) ---
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Game{name='Dark Souls 3', category='Action-RPG', price=20000, quality=89}

--- Juegos con precio entre 10000 y 15000 (Búsqueda Binaria) ---
Game{name='Stardew Valley', category='Simulation', price=10000, quality=96}
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}
Game{name='Celeste', category='Platformer', price=12000, quality=92}
Game{name='Hades', category='Roguelike', price=15000, quality=93}

--- Juegos con precio entre 50000 y 60000 (Búsqueda Binaria) ---
Juegos encontrados: 0

--- Ordenando por Categoría usando Collections.sort ---
Sorted by category using collections.sort. Time: 195876 ns. Size: 10
Game{name='Red Dead Redemption 2', category='Action-Adventure', price=30000, quality=98}
Game{name='Zelda: BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Dark Souls 3', category='Action-RPG', price=20000, quality=89}
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}
Game{name='Celeste', category='Platformer', price=12000, quality=92}
Game{name='The Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Cyberpunk 2077', category='RPG', price=45000, quality=70}
Game{name='Hades', category='Roguelike', price=15000, quality=93}
Game{name='Stardew Valley', category='Simulation', price=10000, quality=96}
Ordenado por: category

--- Juegos de categoría RPG (Búsqueda Binaria) ---
Game{name='Cyberpunk 2077', category='RPG', price=45000, quality=70}
Game{name='The Witcher 3', category='RPG', price=25000, quality=95}

--- Ordenando por Calidad usando QuickSort ---
Sorted by quality using quicksort. Time: 42339 ns. Size: 10
Game{name='Cyberpunk 2077', category='RPG', price=45000, quality=70}
Game{name='Dark Souls 3', category='Action-RPG', price=20000, quality=89}
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Game{name='Celeste', category='Platformer', price=12000, quality=92}
Game{name='Hades', category='Roguelike', price=15000, quality=93}
Game{name='The Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Stardew Valley', category='Simulation', price=10000, quality=96}
Game{name='Zelda: BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Red Dead Redemption 2', category='Action-Adventure', price=30000, quality=98}
Ordenado por: quality

--- Juegos con calidad 90 (Búsqueda Binaria) ---
Game{name='Hollow Knight', category='Metroidvania', price=10000, quality=90}
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}

--- Volviendo a desordenar (simulado) y búsqueda lineal ---
Dataset desordenado. Buscando juegos de categoría 'FPS' (Lineal):
Dataset no ordenado por categoría. Usando búsqueda lineal para getGamesByCategory.
Game{name='Doom Eternal', category='FPS', price=20000, quality=90}
Ordenado por (debería ser null o similar): null
java -cp bin com.example.gamedata.Dataset
--- Probando la Clase Dataset ---
Dataset inicial (null):
Game{name='Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Game{name='RDR2', category='Action-Adventure', price=30000, quality=98}
Game{name='Doom', category='FPS', price=20000, quality=90}
Game{name='Zelda BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Stardew', category='Simulation', price=10000, quality=96}
Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}

--- Pruebas getGamesByPrice ---
Buscando precio 20000 (lineal):
Dataset no ordenado por precio. Usando búsqueda lineal para getGamesByPrice.
  Game{name='Doom', category='FPS', price=20000, quality=90}
  Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Sorted by price using bubblesort. Time: 57457 ns. Size: 7

Dataset ordenado por precio (price)
Game{name='Stardew', category='Simulation', price=10000, quality=96}
Game{name='Doom', category='FPS', price=20000, quality=90}
Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Game{name='Witcher 3', category='RPG', price=25000, quality=95}
Game{name='RDR2', category='Action-Adventure', price=30000, quality=98}
Game{name='Zelda BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Buscando precio 20000 (binaria):
  Game{name='Doom', category='FPS', price=20000, quality=90}
  Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Buscando precio 99999 (no existente, binaria): 0 encontrados.

--- Pruebas getGamesByPriceRange ---
Buscando rango 10000-25000 (lineal):
Dataset no ordenado por precio. Usando búsqueda lineal para getGamesByPriceRange.
  Game{name='Witcher 3', category='RPG', price=25000, quality=95}
  Game{name='Doom', category='FPS', price=20000, quality=90}
  Game{name='Stardew', category='Simulation', price=10000, quality=96}
  Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Sorted by price using insertionsort. Time: 65533 ns. Size: 7

Dataset ordenado por precio (price)
Buscando rango 10000-25000 (binaria):
  Game{name='Stardew', category='Simulation', price=10000, quality=96}
  Game{name='Doom', category='FPS', price=20000, quality=90}
  Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
  Game{name='Witcher 3', category='RPG', price=25000, quality=95}
Buscando rango 60000-70000 (no existente, binaria): 0 encontrados.

--- Pruebas getGamesByCategory ---
Buscando categoría 'RPG' (lineal):
Dataset no ordenado por categoría. Usando búsqueda lineal para getGamesByCategory.
  Game{name='Witcher 3', category='RPG', price=25000, quality=95}
  Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Sorted by category using selectionsort. Time: 77805 ns. Size: 7

Dataset ordenado por categoría (category)
Game{name='RDR2', category='Action-Adventure', price=30000, quality=98}
Game{name='Zelda BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Game{name='Doom', category='FPS', price=20000, quality=90}
Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Game{name='Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Stardew', category='Simulation', price=10000, quality=96}
Buscando categoría 'RPG' (binaria):
  Game{name='Witcher 3', category='RPG', price=25000, quality=95}
  Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Buscando categoría 'Puzzle' (no existente, binaria): 0 encontrados.

--- Pruebas getGamesByQuality ---
Buscando calidad 90 (lineal):
Dataset no ordenado por calidad. Usando búsqueda lineal para getGamesByQuality.
  Game{name='Doom', category='FPS', price=20000, quality=90}
Sorted by quality using mergesort. Time: 109495 ns. Size: 7

Dataset ordenado por calidad (quality)
Game{name='Cyberpunk', category='RPG', price=45000, quality=70}
Game{name='Dark Souls', category='Action-RPG', price=20000, quality=89}
Game{name='Doom', category='FPS', price=20000, quality=90}
Game{name='Witcher 3', category='RPG', price=25000, quality=95}
Game{name='Stardew', category='Simulation', price=10000, quality=96}
Game{name='Zelda BOTW', category='Action-Adventure', price=40000, quality=97}
Game{name='RDR2', category='Action-Adventure', price=30000, quality=98}
Buscando calidad 90 (binaria):
  Game{name='Doom', category='FPS', price=20000, quality=90}
Buscando calidad 50 (no existente, binaria): 0 encontrados.

--- Pruebas sortByAlgorithm ---
Original:
  Witcher 3 - 25000
  Cyberpunk - 45000
  RDR2 - 30000
  Doom - 20000
  Zelda BOTW - 40000
  Stardew - 10000
  Dark Souls - 20000
Sorted by price using quicksort. Time: 25097 ns. Size: 7

Ordenado por precio (quickSort):
  Stardew - 10000
  Doom - 20000
  Dark Souls - 20000
  Witcher 3 - 25000
  RDR2 - 30000
  Zelda BOTW - 40000
  Cyberpunk - 45000
Sorted by price using bubblesort. Time: 9538 ns. Size: 7
Sorted by category using bubblesort. Time: 61586 ns. Size: 7

Ordenado por categoria (bubbleSort):
  RDR2 - Action-Adventure
  Zelda BOTW - Action-Adventure
  Dark Souls - Action-RPG
  Doom - FPS
  Witcher 3 - RPG
  Cyberpunk - RPG
  Stardew - Simulation
Sorted by quality using unknownalgorithm. Time: 137797 ns. Size: 7

Ordenado por calidad (Collections.sort - fallback):
  Cyberpunk - 70
  Dark Souls - 89
  Doom - 90
  Witcher 3 - 95
  Stardew - 96
  Zelda BOTW - 97
  RDR2 - 98
Sorted by price using insertionsort. Time: 23464 ns. Size: 7

Ordenado por atributo desconocido (debería ser price, insertionSort):
  Stardew - 10000
  Dark Souls - 20000
  Doom - 20000
  Witcher 3 - 25000
  RDR2 - 30000
  Zelda BOTW - 40000
  Cyberpunk - 45000

--- Fin de Pruebas Dataset ---
java -cp bin com.example.gamedata.Game
--- Probando la Clase Game ---
Juego 1 (creado): Game{name='Epic Adventure', category='Aventura', price=29990, quality=85}
Nombre: Epic Adventure
Categoría: Aventura
Precio: 29990
Calidad: 85

Modificando Juego 1 con setters...
Juego 1 (modificado): Game{name='Super Epic Adventure', category='Aventura', price=35000, quality=90}

Comparando game1 y game2 (deberían ser iguales): true
Comparando game1 y game3 (deberían ser diferentes): false
HashCode game1: -1980226953
HashCode game2: -1980226953
HashCode game3: -2116640209