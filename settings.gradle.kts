pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        // We can't use libs refs in settings, so this is not stored in libs.versions.toml.
        id("dev.detekt") version "2.0.0-alpha.6"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
}

rootProject.name = "skyzen"

stonecutter {
    create(rootProject) {
        version("26.1", "26.1.2")
    }
}

val supportedVersions = listOf("26.1")

dependencyResolutionManagement {
    versionCatalogs {
        supportedVersions.forEach {
            create("versionedLibs${it.replace(".", "")}") {
                from(files("gradle/${it.replace(".", "_")}.versions.toml"))
            }
        }
    }
}
