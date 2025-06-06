pluginManagement {
    repositories {
        google()
        mavenCentral() // 🔥 needed for KSP
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "NoteZen"
include(":app")
