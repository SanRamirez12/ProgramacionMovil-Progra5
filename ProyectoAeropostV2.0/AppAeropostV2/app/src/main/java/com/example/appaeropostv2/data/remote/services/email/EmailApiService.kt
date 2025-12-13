package com.example.appaeropostv2.data.remote.services.email

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class EmailAttachmentDto(
    val filename: String,
    val mimeType: String,
    val base64: String
)

@Serializable
data class SendEmailRequest(
    val to: String,
    val subject: String,
    val html: String? = null,
    val text: String? = null,
    val attachments: List<EmailAttachmentDto> = emptyList()
)

@Serializable
data class SendEmailResponse(
    val ok: Boolean,
    val messageId: String? = null,
    val error: String? = null,
    val details: String? = null
)


interface EmailApiService {
    @POST("api/email/send")
    suspend fun sendEmail(@Body body: SendEmailRequest): SendEmailResponse
}
