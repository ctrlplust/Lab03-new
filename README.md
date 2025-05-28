echo "# Lab03-new

## Compilación

Compila todos los archivos Java del proyecto:

\`\`\`bash
javac -d bin src/main/java/com/example/gamedata/*.java
\`\`\`

## Ejecución

Ejecuta el benchmark principal:

\`\`\`bash
java -cp bin com.example.gamedata.BenchmarkRunner
\`\`\`
" > README.md

# Compila todo el proyecto
mkdir -p bin
javac -d bin src/main/java/com/example/gamedata/*.java