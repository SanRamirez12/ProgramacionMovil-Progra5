package com.example.appaeropostv2.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import com.example.appaeropostv2.data.remote.services.email.EmailAttachmentDto
import com.example.appaeropostv2.data.remote.services.email.EmailApiService
import com.example.appaeropostv2.data.remote.services.email.SendEmailRequest
import com.example.appaeropostv2.domain.common.Resource
import com.example.appaeropostv2.interfaces.InterfaceEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryEmail(
    private val api: EmailApiService,
    private val contentResolver: ContentResolver
) : InterfaceEmail {

    override fun sendEmail(
        to: String,
        subject: String,
        html: String?,
        text: String?
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val resp = api.sendEmail(
                SendEmailRequest(to = to, subject = subject, html = html, text = text)
            )
            if (resp.ok) emit(Resource.Success(true))
            else emit(Resource.Error(resp.error ?: "El backend respondió ok=false"))
        } catch (e: Exception) {
            emit(Resource.Error("No se pudo enviar el correo.", e))
        }
    }

    fun sendEmailWithPdf(
        to: String,
        subject: String,
        html: String?,
        text: String?,
        pdfUri: Uri,
        filename: String = "Factura_Aeropost.pdf"
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val bytes = contentResolver.openInputStream(pdfUri)?.use { it.readBytes() }
                ?: run {
                    emit(Resource.Error("No se pudo leer el PDF para adjuntarlo."))
                    return@flow
                }

            val b64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

            val req = SendEmailRequest(
                to = to,
                subject = subject,
                html = html,
                text = text,
                attachments = listOf(
                    EmailAttachmentDto(
                        filename = filename,
                        mimeType = "application/pdf",
                        base64 = b64
                    )
                )
            )

            val resp = api.sendEmail(req)
            if (resp.ok) emit(Resource.Success(true))
            else emit(Resource.Error(resp.error ?: "El backend respondió ok=false"))
        } catch (e: Exception) {
            emit(Resource.Error("No se pudo enviar el correo con adjunto: ${e.message}", e))
        }
    }
}
