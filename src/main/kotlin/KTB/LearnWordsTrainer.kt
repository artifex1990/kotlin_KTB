package KTB

import java.io.File

private const val SEPARATOR = '|'
private const val EXCEPTION_INCORRECT_FILE = "Некорректный файл"

class LearnWordsTrainer(
    private val learnedAnswerCount: Int = 3,
    private val countOfQuestionWords: Int = 4,
) {
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

        try {
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
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException(EXCEPTION_INCORRECT_FILE)
        }


    }

    private fun saveDictionary() {
        this.fileWords.writeText("")

        this.dictionary.forEach { word ->
            this.fileWords.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }

    fun getStatistics(): Statistics {
        val learnedCount = this.dictionary.filter { word -> word.correctAnswersCount >= this.learnedAnswerCount }.size
        val totalCount = this.dictionary.size
        val percent = (100.0 * learnedCount / totalCount).toInt()

        return Statistics(learnedCount, totalCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = this.dictionary.filter { word ->
            word.correctAnswersCount < this.learnedAnswerCount
        }

        if (notLearnedList.isEmpty()) {
            return null
        }

        val questionWords = if (notLearnedList.size < this.countOfQuestionWords) {
            val learnedList = this.dictionary.filter { word ->
                word.correctAnswersCount >= this.learnedAnswerCount
            }.shuffled()
            notLearnedList.shuffled().take(countOfQuestionWords) + learnedList.take(
                this.countOfQuestionWords - notLearnedList.size
            )
        } else {
            notLearnedList.shuffled().take(this.countOfQuestionWords)
        }.shuffled()

        val correctAnswer = questionWords.random()

        this.question = Question(
            questionWords,
            correctAnswer
        )

        return this.question
    }

    fun checkAnswer(userAnswerId: Int): Boolean = when {
        userAnswerId !in 1..this.countOfQuestionWords -> {
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