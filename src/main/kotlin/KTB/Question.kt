package KTB

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

fun Question.questionToString(): String {
    var questionString = "${this.correctAnswer.original}:\n"

    this.variants.forEachIndexed { i, word ->
        questionString += "${i + 1} - ${word.translate}\n"
    }

    questionString += "----------\n"
    questionString += "0 - Меню\n"

    return questionString
}
