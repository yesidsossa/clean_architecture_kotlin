plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w342/\"")
        buildConfigField("String", "IMAGE_URL_LARGE", "\"https://image.tmdb.org/t/p/w1280/\"")
        buildConfigField("String", "THE_MOVIE_API_KEY", "\"09f3b3b3c3d08cfbfc2906a2e6c4f087\"")


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("prod") {
            isDefault = true
        }
        create("mock")
    }

    namespace = "com.instaleap.data"
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

dependencies {
    api(project(":domain"))
    testImplementation(project(":core-test"))

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.paging.common.ktx)
    ksp(libs.room.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
}
