package Telegram

const val MAX_TIME_SLEEP: Long = 2000

fun main(args: Array<String>) {
    val telegramBotService = TelegramBotService(args[0])
    var updateId = 0

    println("GetMe")
    println(telegramBotService.getMe())

    println("GetUpdates")
    val updateIdRegex: Regex = "\"update_id\":([0-9]+)[,}]".toRegex()
    val chatIdRegex: Regex = "\"chat\":\\{[^}]*\"id\":([0-9]+)".toRegex()
    val chatTextRegex: Regex = "\"text\":\"([^\"]*)\"".toRegex()

    while (true) {
        Thread.sleep(MAX_TIME_SLEEP)
        val updates: String = telegramBotService.getUpdates(updateId)

        val groupUpdateId = findOnRegex(updateIdRegex, 1, updates)
        val groupChatId = findOnRegex(chatIdRegex, 1, updates)
        val groupChatText = findOnRegex(chatTextRegex, 1, updates)
            ?.let { unescapeJsonText(it) }

        println(updates)

        if (
            groupUpdateId.isNullOrEmpty() ||
            groupChatId.isNullOrEmpty() ||
            groupChatText.isNullOrEmpty()
        ) continue

        println(telegramBotService.sendMessage(groupChatId, groupChatText))

        updateId = groupUpdateId.toInt() + 1
    }
}

fun findOnRegex(regexTemplate: Regex, groupId: Int, updates: String): String? =
    regexTemplate.find(updates)?.groups?.get(groupId)?.value

fun unescapeJsonText(text: String): String =
    text
        .replace("\\\\u([0-9a-fA-F]{4})".toRegex()) {
            it.groupValues[1].toInt(16).toChar().toString()
        }
        .replace("\\\"", "\"")
        .replace("\\\\", "\\")