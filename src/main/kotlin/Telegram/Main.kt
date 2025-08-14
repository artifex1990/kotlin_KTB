package Telegram

const val MAX_TIME_SLEEP: Long = 2000

fun main(args: Array<String>) {
    val telegramBotService = TelegramBotService(args[0])
    var updateId = 0

    println("GetMe")
    println(telegramBotService.getMe())

    println("GetUpdates")
    while(true) {
        Thread.sleep(MAX_TIME_SLEEP)
        val updates: String = telegramBotService.getUpdates(updateId)
        val startUpdateId = updates.lastIndexOf("update_id")
        val endUpdateId = updates.lastIndexOf(",\n\"message\"")

        println(updates)

        if (startUpdateId == -1 || endUpdateId == -1) continue

        val updateIdString = updates.substring(startUpdateId + 11, endUpdateId)
        updateId = updateIdString.toInt() + 1
    }
}