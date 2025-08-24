package Telegram

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val MAX_SIZE_TEXT_MESSAGE = 4096

class TelegramBotService(token: String) {
    private val baseUrl: String = "https://api.telegram.org/bot$token/"
    private val urlGetMe: String = "${baseUrl}getMe"
    private val urlGetUpdates: String = "${baseUrl}getUpdates"
    private val urlSendMessage: String = "${baseUrl}sendMessage"
    private val client: HttpClient = HttpClient.newBuilder().build()

    private fun prepareRequest(url: String): HttpRequest = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .header("Accept", "application/json")
        .build()

    private fun clientSend(request: HttpRequest): String = this.client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    ).body()

    fun getMe(): String = clientSend(this.prepareRequest(this.urlGetMe))
    fun getUpdates(updateId: Int): String = clientSend(
        this.prepareRequest(
            "${this.urlGetUpdates}${"?offset=$updateId"}"
        )
    )
    fun sendMessage(chatId: String, text: String): String? {
        if (text.isEmpty() || text.length > MAX_SIZE_TEXT_MESSAGE) {
            return null;
        }

        val encodedText = URLEncoder.encode(text, "UTF-8")

        return clientSend(
            this.prepareRequest(
                "${this.urlSendMessage}?chat_id=${chatId}&text=${encodedText}"
            )
        )
    }
}
