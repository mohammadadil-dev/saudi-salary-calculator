# Release builds now run with isMinifyEnabled/isShrinkResources = true (see app/build.gradle.kts).
# Room, Hilt, and Compose each ship their own consumer ProGuard rules inside their AARs, and this
# app uses no reflection-based serialization (no Gson/Moshi/kotlinx.serialization/Retrofit), so no
# extra keep rules are required today.
#
# If a release build crashes where debug doesn't, it's almost always R8 stripping something
# accessed only by reflection. Check the mapping file at
# app/build/outputs/mapping/release/mapping.txt and add a targeted -keep rule here.
