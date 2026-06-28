import java.util.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.kapt")
  id("com.google.dagger.hilt.android")
}

// Release signing credentials live outside source control — see PLAY_STORE_RELEASE.md.
// Falls back to null (unsigned release build) if keystore.properties isn't present, so CI/clones
// without the file can still build a release artifact (just won't be upload-ready).
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
  if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { load(it) }
  }
}

android {
  namespace = "com.saudi.salarycalculator"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.saudi.salarycalculator"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables { useSupportLibrary = true }

    // Google's public sample AdMob App ID/ad unit — safe to ship in debug builds, and the
    // default for release until replaced below. See PLAY_STORE_RELEASE.md before publishing.
    manifestPlaceholders["admob_app_id"] = "ca-app-pub-3940256099942544~3347511713"
    buildConfigField("String", "BANNER_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/9214589741\"")
  }

  signingConfigs {
    create("release") {
      if (keystorePropertiesFile.exists()) {
        storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
        storePassword = keystoreProperties.getProperty("storePassword")
        keyAlias = keystoreProperties.getProperty("keyAlias")
        keyPassword = keystoreProperties.getProperty("keyPassword")
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      // Using the non-optimizing default file (not "-optimize.txt"): still shrinks/obfuscates,
      // just skips ProGuard/R8's bytecode-level optimization passes. The "-optimize" variant
      // combined with this app's Compose+Lifecycle version mix was crashing release builds with
      // "CompositionLocal LocalLifecycleOwner not present" — see proguard-rules.pro for details.
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
      )
      if (keystorePropertiesFile.exists()) {
        signingConfig = signingConfigs.getByName("release")
      }

      // Real AdMob App ID / banner ad unit ID (set 2026-06-28). Debug builds intentionally keep
      // Google's sample IDs above — only release ships the real ones.
      manifestPlaceholders["admob_app_id"] = "ca-app-pub-8890346685665889~3172188319"
      buildConfigField("String", "BANNER_AD_UNIT_ID", "\"ca-app-pub-8890346685665889/1725199104\"")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions { jvmTarget = "17" }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.14"
  }

  packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
  implementation(project(":feature:calculator"))
  implementation(project(":core:data"))
  implementation(project(":core:database"))
  implementation(project(":core:preferences"))
  implementation(project(":core:designsystem"))

  implementation(platform("androidx.compose:compose-bom:2024.06.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-compose:2.7.7")
  implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
  implementation("androidx.activity:activity-compose:1.9.0")
  implementation("androidx.core:core-splashscreen:1.0.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

  implementation("com.google.dagger:hilt-android:2.52")
  kapt("com.google.dagger:hilt-compiler:2.52")

  implementation("com.google.android.gms:play-services-ads:23.2.0")

  debugImplementation("androidx.compose.ui:ui-tooling")
}

kapt {
  correctErrorTypes = true
}
