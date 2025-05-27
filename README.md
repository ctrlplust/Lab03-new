# Compilar todos los archivos Java
mkdir -p bin
javac -d bin src/main/java/com/example/gamedata/*.java

# Ejecutar Main
java -cp bin com.example.gamedata.Main

# Ejecutar Dataset
java -cp bin com.example.gamedata.Dataset

# Ejecutar Game
java -cp bin com.example.gamedata.Game

# Ejecutar BenchmarkRunner
java -cp bin com.example.gamedata.BenchmarkRunner

# Ejecutar SearchBenchmarkRunner
java -cp bin com.example.gamedata.SearchBenchmarkRunner

# Ejecutar GenerateData
java -cp bin com.example.gamedata.GenerateData
