package KTB

import java.io.File

private const val SEPARATOR = '|'
private const val MAX_WORDS_FOR_KNOWS = 3
private const val MAX_WORDS_FOR_EXERCISE = 4

class LearnWordsTrainer {
    private val dictionary: MutableList<Word> = mutableListOf()
    private val fileWords: File = File("words.txt")
    private var question: Question? = null

    init {
        if (!this.fileWords.exists()) {
            this.fileWords.createNewFile()

            this.fileWords.appendText("hello${SEPARATOR}привет${SEPARATOR}0\n")
            this.fileWords.appendText("dog${SEPARATOR}собака\n")
            this.fileWords.appendText("fish${SEPARATOR}рыба${SEPARATOR}0\n")
            this.fileWords.appendText("chicken${SEPARATOR}курица${SEPARATOR}0\n")
            this.fileWords.appendText("cat${SEPARATOR}кошка${SEPARATOR}0\n")
        }

        val lines: List<String> = this.fileWords.readLines()
        for (line in lines) {
            val parts = line.split(SEPARATOR)
            this.dictionary.add(
                Word(
                    original = parts[0],
                    translate = parts[1],
                    correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
                )
            )
        }
    }

    private fun saveDictionary() {
        this.fileWords.writeText("")

        this.dictionary.forEach { word ->
            this.fileWords.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }

    fun getStatistics(): Statistics {
        val learnedCount = this.dictionary.filter { word -> word.correctAnswersCount >= MAX_WORDS_FOR_KNOWS }.size
        val totalCount = this.dictionary.size
        val percent = (100.0 * learnedCount / totalCount).toInt()

        return Statistics(learnedCount, totalCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = this.dictionary.filter { word ->
            word.correctAnswersCount < MAX_WORDS_FOR_KNOWS
        }

        if (notLearnedList.isEmpty()) {
            return null
        }

        val questionWords = notLearnedList.shuffled().take(MAX_WORDS_FOR_EXERCISE)
        val correctAnswer = questionWords.random()

        question = Question(
            questionWords,
            correctAnswer
        )

        return question
    }

    fun checkAnswer(userAnswerId: Int): Boolean = when {
        userAnswerId !in 1..MAX_WORDS_FOR_EXERCISE -> {
            println("Неправильный ввод!")
            false
        }

        this.question?.correctAnswer != this.question?.variants[userAnswerId.minus(1)] -> {
            println(
                "Неправильно! ${this.question?.correctAnswer?.original} " +
                        "- это ${this.question?.correctAnswer?.translate}"
            )
            false
        }

        else -> {
            println("Правильно!")
            this.question?.correctAnswer?.correctAnswersCount++
            this.saveDictionary()
            true
        }
    }
}