package KTB

import java.io.File
import kotlin.random.Random

const val SEPARATOR = '|'
const val MAX_WORDS_FOR_KNOWS = 3
const val MAX_WORDS_FOR_EXERCISE = 4

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
            "1" -> {
                while (true) {
                    println()
                    val notLearnedList = dictionary.filter { word -> word.correctAnswersCount < MAX_WORDS_FOR_KNOWS }

                    if (notLearnedList.isEmpty()) {
                        println("Все слова в словаре выучены")
                        break
                    }

                    val questionWords = notLearnedList.shuffled().take(MAX_WORDS_FOR_EXERCISE)
                    val correctAnswerId = Random.nextInt(0, questionWords.size)

                    println("${questionWords[correctAnswerId].original}:")
                    questionWords.forEachIndexed { i, word ->
                        println("${i + 1} - ${word.translate}")
                    }
                    println("----------")
                    println("0 - Меню")

                    print("Введите ваш ответ: ")
                    val userAnswerInput = readln().toInt()

                    when {
                        userAnswerInput == 0 -> break

                        userAnswerInput !in 1..MAX_WORDS_FOR_EXERCISE -> {
                            println("Неправильный ввод!")
                            continue
                        }

                        correctAnswerId != userAnswerInput - 1 -> {
                            println(
                                "Неправильно! ${questionWords[correctAnswerId].original} " +
                                        "- это ${questionWords[correctAnswerId].translate}"
                            )
                            continue
                        }

                        else -> {
                            println("Правильно!")
                            questionWords[correctAnswerId].correctAnswersCount++
                            saveDictionary(dictionary)
                        }
                    }
                }
            }

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
        fileWords.appendText("fish${SEPARATOR}рыба${SEPARATOR}0\n")
        fileWords.appendText("chicken${SEPARATOR}курица${SEPARATOR}0\n")
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

fun saveDictionary(dictionary: List<Word>) {
    val fileWords: File = File("words.txt")
    fileWords.writeText("")

    dictionary.forEach { word ->
        fileWords.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
    }
}
