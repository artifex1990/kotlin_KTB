package Telegram

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TelegramBotService(token: String) {
    private val baseUrl: String = "https://api.telegram.org/bot$token/"
    private val urlGetMe: String = "${baseUrl}getMe"
    private val urlGetUpdates: String = "${baseUrl}getUpdates"
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
    fun getUpdates(): String = clientSend(this.prepareRequest(this.urlGetUpdates))
}
