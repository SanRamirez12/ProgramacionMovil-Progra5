plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // ← KSP desde el catalog
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
        // Si luego usas instrumentationRunner, decláralo aquí
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions { jvmTarget = "11" }

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
    // BOM en todos los classpaths relevantes
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(platform(libs.androidx.compose.bom))

    // Base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.compose.material.icons)

    // Herramientas/debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)   // ← usa el alias del TOML (ya corregido)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Material & Splash
    implementation(libs.material)
    implementation(libs.splash)

    // Desugaring
    coreLibraryDesugaring(libs.desugar)

    // Room (KSP)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    // androidTestImplementation(libs.androidx.compose.ui.test.junit4) // cuando hagas tests UI
}




