package KTB

import java.io.File

fun main() {
    val fileWords: File = File("words.txt")
    if (!fileWords.exists()) {
        fileWords.createNewFile()

        fileWords.appendText("hello привет\n")
        fileWords.appendText("dog собака\n")
        fileWords.appendText("cat кошка\n")
    }

    println(fileWords.readLines())
}

