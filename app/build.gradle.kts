plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // <--- ¡AÑADE ESTA LÍNEA!
}
android {
    namespace = "com.example.Sin_Riesgo"
    compileSdk = 35 // <--- Asegúrate de que sea 35 o la última que tenías

    defaultConfig {
        applicationId = "com.example.Sin_Riesgo" // <--- ¡VERIFICA ESTO! Debe ser tu package name
        minSdk = 24 // <--- El mínimo que tenías
        targetSdk = 35 // <--- El target que tenías
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11 // <--- El que tenías
        targetCompatibility = JavaVersion.VERSION_11 // <--- El que tenías
    }
    kotlinOptions {
        jvmTarget = "11" // <--- El que tenías
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    // Dependencias base del nuevo proyecto (normalmente ya están)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended) // Podría ser material-icons-extended, verifica en tu viejo libs.versions.toml
    // Si tu viejo proyecto usaba constraint.layout, el nuevo podría no tenerlo por defecto
    implementation(libs.constraint.layout) // <--- Añadir si usas ConstraintLayout en Compose

    // Tus dependencias específicas
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json) // <--- ¡AÑADIR ESTA LÍNEA!

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("io.coil-kt:coil-compose:2.7.0") // <--- ¡AÑADIR ESTA LÍNEA para Coil!

    // Dependencias de prueba (normalmente ya están)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}