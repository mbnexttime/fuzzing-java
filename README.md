1. собрать utbot.jar из https://github.com/UnitTestBot/UTBotJava
2. положить его в корень
3. собрать артефакт со всеми зависимостями
4. положить его в корень с именем fuzzing.jar
5. jar uvfm fuzzing.jar META-INF/MANIFEST.MF
6. можно делать java -jar fuzzing.jar ...