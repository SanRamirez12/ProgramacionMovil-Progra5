package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.common.Resource
import kotlinx.coroutines.flow.Flow

interface InterfaceEmail {
    fun sendEmail(to: String, subject: String, html: String?, text: String?): Flow<Resource<Boolean>>
}