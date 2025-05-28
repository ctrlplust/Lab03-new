# Compilar todos los archivos Java
``
mkdir -p bin
``
 
Creas la carpeta bin para guardar todos los .class, y poder compilar el codigo.

si es que lo quieres eliminar:

``
rm -r bin
``

Compilar todos las clases dentro de com/example/gamedata

``
javac -d bin src/main/java/com/example/gamedata/*.java
``
# Ejecutar Main
``
java -cp bin com.example.gamedata.Main
``

# Ejecutar Dataset
``
java -cp bin com.example.gamedata.Dataset
``
# Ejecutar Game
``
java -cp bin com.example.gamedata.Game
``
# Ejecutar BenchmarkRunner
``
java -cp bin com.example.gamedata.BenchmarkRunner
``
# Ejecutar SearchBenchmarkRunner
``
java -cp bin com.example.gamedata.SearchBenchmarkRunner
``
# Ejecutar GenerateData
``
java -cp bin com.example.gamedata.GenerateData
``
Esta generada en el repositorio si quieres eliminarla
``
rm -r data_generated
``

# Ejecutar CoutingSort
``
java -cp bin com.example.gamedata.DatasetCountingSort
``
