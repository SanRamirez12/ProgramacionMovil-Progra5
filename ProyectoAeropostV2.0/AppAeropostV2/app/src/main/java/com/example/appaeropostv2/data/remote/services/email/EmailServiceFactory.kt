package com.example.appaeropostv2.data.remote.services.email

import com.example.appaeropostv2.core.network.RetrofitProvider

object EmailServiceFactory {
    fun create(baseUrl: String): EmailApiService {
        return RetrofitProvider.create(baseUrl).create(EmailApiService::class.java)
    }
}
