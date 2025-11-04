plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.practicageolocalizador"

    // Android 14 estable y ampliamente compatible
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.practicageolocalizador"
        minSdk = 21      // amplia compatibilidad; si quieres, usa 23
        targetSdk = 36   // alineado con compileSdk 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Si usas vectores en minSdk < 21, habilita soporte (no necesario aquí)
        // vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Google Play Services - Fused Location Provider
    implementation("com.google.android.gms:play-services-location:21.3.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}

