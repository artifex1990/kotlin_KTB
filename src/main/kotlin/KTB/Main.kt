package KTB

fun main() {
    val trainer = LearnWordsTrainer()

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
                    val question = trainer.getNextQuestion()

                    if (question == null) {
                        println("Все слова в словаре выучены")
                        break
                    }

                    println(question.questionToString())

                    print("Введите ваш ответ: ")
                    val userAnswerInput = readln().toInt()

                    if (userAnswerInput == 0) break

                    trainer.checkAnswer(userAnswerInput)
                }
            }

            "2" -> {
                println("Статистика")
                val statistics = trainer.getStatistics()
                println("Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | ${statistics.percent}%")
            }

            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }
        println()
    }
}

