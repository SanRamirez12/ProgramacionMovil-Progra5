package com.example.appaeropostv2.domain.model

data class SystemInfo(
    val appName: String,
    val versionName: String,
    val versionCode: Int,
    val buildType: String,
    val packageName: String,
    val deviceModel: String,
    val androidVersion: String
)