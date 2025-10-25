plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"
}

android {
    namespace = "com.example.appaeropost"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appaeropost"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // (Solo si usaras KAPT; con KSP no hace falta)
        // javaCompileOptions {
        //   annotationProcessorOptions {
        //       arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
        //   }
        // }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions { jvmTarget = "11" }

    buildFeatures { compose = true }
}

// ⬇ ESTA SECCIÓN ES CLAVE PARA EL ERROR DEL SCHEMA
ksp {
    // carpeta donde Room exportará los esquemas (crea /app/schemas en el proyecto)
    arg("room.schemaLocation", "$projectDir/schemas")
    // opcional pero útil
    arg("room.generateKotlin", "true")
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Íconos Material
    implementation("androidx.compose.material:material-icons-extended")

    // ViewModel ↔ Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Necesarias para teclado/acciones de texto en Compose
    implementation("androidx.compose.ui:ui-text")
    implementation("androidx.compose.foundation:foundation")

    // Splash
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.12.0")

    // Desugaring (java.time en API < 26)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Room (con KSP)
    val room = "2.6.1"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    ksp("androidx.room:room-compiler:$room")
}
