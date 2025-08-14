package Telegram

fun main(args: Array<String>) {
    val telegramBotService = TelegramBotService(args[0])

    println("GetMe")
    println(telegramBotService.getMe())

    println("GetUpdates")
    println(telegramBotService.getUpdates())
}