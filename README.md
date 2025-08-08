Использованы Jackson, Lombok, Slf4j. Все они помещены в папку libs

Для компиляции через Power Shell:

javac -d target/classes -cp "libs/*" `
    src/main/java/org/example/*.java `
    src/main/java/org/example/model/*.java `
    src/main/java/org/example/util/*.java


Для компиляции через cmd:

javac -d target/classes -cp "libs/*" src/main/java/org/example/*.java src/main/java/org/example/model/*.java src/main/java/org/example/util/*.java



Для компиляции Linux/Mac:

javac -d target/classes -cp "libs/*" \
    src/main/java/org/example/*.java \
    src/main/java/org/example/model/*.java \
    src/main/java/org/example/util/*.java



Для запуска на Windows:

java -cp "target/classes;libs/*" org.example.TicketReaderApplication "<Передаем путь к Json>"



Для запуска на Mac/Linux:

java -cp "target/classes:libs/*" org.example.TicketReaderApplication "Передаем путь к Json"
