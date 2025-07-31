package KTB

import java.io.File

const val SEPARATOR = '|'
const val MAX_WORDS_FOR_KNOWS = 3

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun main() {
    val dictionary: List<Word> = loadDictionary()

    while (true) {
        println(
            """
            Меню: 
            1 – Учить слова
            2 – Статистика
            0 – Выход
        """.trimIndent()
        )

        print("Введите пункт: ")
        when (readln()) {
            "1" -> println("Учить слова")
            "2" -> {
                println("Статистика")
                val learnedCount = dictionary.filter { word -> word.correctAnswersCount >= MAX_WORDS_FOR_KNOWS }.size
                val totalCount = dictionary.size
                val percent = (100.0 * learnedCount / totalCount).toInt()

                println("Выучено $learnedCount из $totalCount слов | $percent%")

            }

            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }
        println()
    }
}

fun loadDictionary(): List<Word> {
    val dictionary: MutableList<Word> = mutableListOf()
    val fileWords: File = File("words.txt")

    if (!fileWords.exists()) {
        fileWords.createNewFile()

        fileWords.appendText("hello${SEPARATOR}привет${SEPARATOR}0\n")
        fileWords.appendText("dog${SEPARATOR}собака\n")
        fileWords.appendText("cat${SEPARATOR}кошка${SEPARATOR}0\n")
    }

    val lines: List<String> = fileWords.readLines()
    for (line in lines) {
        val parts = line.split(SEPARATOR)
        dictionary.add(
            Word(
                original = parts[0],
                translate = parts[1],
                correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
            )
        )
    }

    return dictionary
}
