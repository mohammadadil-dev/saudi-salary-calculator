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
  compileSdk = 36

  defaultConfig {
    applicationId = "com.saudi.salarycalculator"
    minSdk = 24
    targetSdk = 36
    versionCode = 7
    versionName = "1.2.0"

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

  // The app has its own in-app language toggle (Settings -> language) instead of following the
  // device's system locale. Without this, Google Play's App Bundle delivery splits the .aab by
  // language and only installs resources matching the device's system locale — so on an
  // English-locale device, the Arabic strings are never even installed and the in-app toggle has
  // nothing to switch to. Disabling the split ships every locale's resources in every install.
  bundle {
    language {
      enableSplit = false
    }
  }
}

dependencies {
  implementation(project(":feature:calculator"))
  implementation(project(":core:model"))
  implementation(project(":core:calculator"))
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
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

  implementation("com.google.dagger:hilt-android:2.52")
  kapt("com.google.dagger:hilt-compiler:2.52")

  implementation("com.google.android.gms:play-services-ads:23.2.0")

  // Force-update flow (see MainActivity) — Google Play's In-App Update API. Only activates for
  // Play-Store-installed builds; has no effect on sideloaded/adb-installed APKs since it checks
  // against the Play Store's own record of what's installed.
  implementation("com.google.android.play:app-update:2.1.0")

  debugImplementation("androidx.compose.ui:ui-tooling")
}

kapt {
  correctErrorTypes = true
}
