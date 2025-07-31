package KTB

import java.io.File

const val SEPARATOR = '|'

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Any = 0,
)

fun main() {
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

    for (word in dictionary) {
        println("""
            Оригинал: ${word.original}
            Перевод: ${word.translate}
            Сколько раз угадали: ${word.correctAnswersCount}
            
        """.trimIndent())
    }
}


