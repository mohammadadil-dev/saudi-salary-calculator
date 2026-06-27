pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "SaudiSalaryCalculator"
include(":app")
include(":core:model")
include(":core:calculator")
include(":core:database")
include(":core:preferences")
include(":core:data")
include(":core:designsystem")
include(":feature:calculator")
