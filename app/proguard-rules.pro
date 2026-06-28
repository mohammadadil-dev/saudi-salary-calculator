# Release builds now run with isMinifyEnabled/isShrinkResources = true (see app/build.gradle.kts).
# Room, Hilt, and Compose each ship their own consumer ProGuard rules inside their AARs, and this
# app uses no reflection-based serialization (no Gson/Moshi/kotlinx.serialization/Retrofit), so no
# extra keep rules are required today.
#
# If a release build crashes where debug doesn't, it's almost always R8 stripping something
# accessed only by reflection. Check the mapping file at
# app/build/outputs/mapping/release/mapping.txt and add a targeted -keep rule here.
#
# One such case already hit (2026-06-28): release builds crashed at launch with
# "IllegalStateException: CompositionLocal LocalLifecycleOwner not present" — entirely inside
# Compose/Lifecycle runtime internals (AndroidComposeView.onAttachedToWindow ->
# setOnViewTreeOwnersAvailable -> LocalLifecycleOwner.current), no app code involved, never
# reproduced in debug. AGP 8.5.2 / R8.
#
# Round 1: disabled android.enableR8.fullMode (gradle.properties) alone — did NOT fix it
# (confirmed by a second crash on a verified fresh rebuild, different obfuscated names).
# Round 2: switched to the non-optimizing default proguard file (proguard-android.txt instead
# of -optimize.txt) and kept androidx.lifecycle.**/androidx.compose.runtime.**/
# androidx.compose.ui.platform.AndroidComposeView explicitly — still crashed. The un-obfuscated
# trace this time pinpointed it precisely: the *read* side (LocalLifecycleOwnerKt's default-value
# lambda) was kept and visible by name, but the *provide* side — the WrappedComposition /
# CompositionLocalProvider plumbing that lives in androidx.compose.ui.platform alongside
# AndroidComposeView — was still obfuscated/optimized because only that one class in the package
# was kept, not the whole package. Round 3 (below): keep that entire package too, so none of the
# Compose<->Lifecycle interop machinery is touched by R8 at all, only the app's own code.
-keep class androidx.lifecycle.** { *; }
-keep interface androidx.lifecycle.** { *; }
-keepclassmembers class androidx.lifecycle.** { *; }
-keep class androidx.activity.** { *; }
-keep interface androidx.activity.** { *; }
-keep class androidx.compose.ui.platform.** { *; }
-keep interface androidx.compose.ui.platform.** { *; }
-keepclassmembers class androidx.compose.ui.platform.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep interface androidx.compose.runtime.** { *; }
-keepclassmembers class androidx.compose.runtime.** { *; }
-dontwarn androidx.lifecycle.**
-dontwarn androidx.compose.**
