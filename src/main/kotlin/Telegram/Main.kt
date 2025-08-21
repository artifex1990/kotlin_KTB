package Telegram

const val MAX_TIME_SLEEP: Long = 2000

fun main(args: Array<String>) {
    val telegramBotService = TelegramBotService(args[0])
    var updateId = 0

    println("GetMe")
    println(telegramBotService.getMe())

    println("GetUpdates")
    val updateIdRegex: Regex = "\"update_id\":([0-9]+)".toRegex()
    
    while(true) {
        Thread.sleep(MAX_TIME_SLEEP)
        val updates: String = telegramBotService.getUpdates(updateId)
        
        val matchResult: MatchResult? = updateIdRegex.find(updates)
        val groupsString = matchResult?.groups?.get(1)?.value

        println(updates)

        if (groupsString.isNullOrEmpty()) continue

        updateId = groupsString.toInt() + 1
    }
}