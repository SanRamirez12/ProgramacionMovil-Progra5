plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // KSP desde el catalog
}

android {
    namespace = "com.example.appaeropostv2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appaeropostv2"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Para que Room exporte/lea esquemas en tests
    sourceSets {
        getByName("androidTest") {
            assets.srcDirs(files("$projectDir/schemas"))
        }
    }
}

// Export de esquemas de Room (útil para migraciones)
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.generateKotlin", "true")
}

dependencies {
    // BOM de Compose
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(platform(libs.androidx.compose.bom))

    // Base AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text)

    // Íconos de Material (extended)
    implementation(libs.compose.material.icons)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Material (views clásicas) & Splash
    implementation(libs.material)
    implementation(libs.splash)

    // Desugaring (para LocalDate, etc.)
    coreLibraryDesugaring(libs.desugar)

    // Room (con KSP)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Herramientas / debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    // Cuando quieras tests de UI:
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}





