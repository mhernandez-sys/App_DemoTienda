plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(files("libs\\jxl.jar"))
    implementation(files("libs\\ksoap2-android-assembly-3.6.4-jar-with-dependencies.jar"))
    implementation(files("libs\\poi-3.12-android-a.jar"))
    implementation(files("libs\\poi-ooxml-schemas-3.12-20150511-a.jar"))
    implementation(files("libs\\Sewoo_Android_1113.jar"))
    implementation(files("libs\\xUtils-2.5.5.jar"))
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\vamaf\\AndroidStudioProjects\\gitdemo\\demo2\\app\\libs",
        "include" to listOf("*.aar", "*.jar"))))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.retrofit2:loggin-interceptor:4.11.0")
}