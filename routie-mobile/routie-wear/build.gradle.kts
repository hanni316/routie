plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.routie_wear"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.routie_wear"
        minSdk = 30  // ✅ Wear OS는 minSdk 30이 기본
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.android.gms:play-services-wearable:19.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ✅ 최신 Compose BOM 유지
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.wear.compose:compose-material")
    implementation("androidx.wear.compose:compose-foundation")

    // 네비게이션 최신화 (모바일과 동일한 버전)
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.wear.compose:compose-navigation:1.2.0")  // ✅ Wear OS 네비게이션 추가

    //Accompanist 테마 어댑터 유지 (Material 3)
    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.31.1-alpha")
    implementation("com.google.accompanist:accompanist-themeadapter-material:0.31.1-alpha")

    //Health Services API (운동 데이터 수집)
    implementation("androidx.health:health-services-client:1.1.0-alpha05")

    //Wear OS 필수 라이브러리
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear:wear-input:1.1.0")

    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.1")
    implementation("androidx.wear.watchface:watchface-complications-data-source:1.2.1")
    implementation("androidx.wear.watchface:watchface:1.2.1")

}