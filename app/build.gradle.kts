import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.gradle.ktlint)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.protobuf)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.team25"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.team25"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
        buildConfigField("String", "S3_ACCESS_KEY", getApiKey("S3_ACCESS_KEY"))
        buildConfigField("String", "S3_SECRET_KEY", getApiKey("S3_SECRET_KEY"))
        buildConfigField("String", "API_BASE_URL", getApiUrl("API_BASE_URL"))
        manifestPlaceholders["kakaoApiKey"] = getApiKey("KAKAO_API_KEY_NO_QUOTES")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.google.play.services.location)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.dagger.hilt.android)
    implementation(libs.kakao.sdk.all)
    implementation(libs.kakao.map)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.glide)
    ksp(libs.compiler)
    implementation(libs.aws.android.sdk.s3)
    implementation(libs.aws.android.sdk.mobile.client)
    implementation(libs.aws.android.sdk.core)

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

fun getApiKey(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key, "")
fun getApiUrl(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key, "")

