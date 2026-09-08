import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
}

val versionedLibs = the<VersionCatalogsExtension>()
    .find("versionedLibs${sc.current.project.replace(".", "")}")
    .get()

fun VersionCatalog.lib(name: String) = findLibrary(name).get()
fun VersionCatalog.ver(name: String) = findVersion(name).get()

version = "${providers.gradleProperty("mod_version").get()}+${sc.current.project}"
group = providers.gradleProperty("maven_group").get()
base.archivesName = rootProject.name

sourceSets {
    main {
        kotlin {
            srcDir("src/main/fabric")
        }
        java {
            srcDir("src/main/java")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.notenoughupdates.org/releases/")
}

dependencies {
    minecraft(versionedLibs.lib("minecraft"))

    implementation(libs.fabric.loader)
    implementation(libs.fabric.language.kotlin)
    implementation(versionedLibs.lib("fabric-api"))

    implementation(versionedLibs.lib("moulconfig"))
    include(versionedLibs.lib("moulconfig"))
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mc_range", versionedLibs.ver("mc-range"))

    filesMatching("fabric.mod.json") {
        val props = mapOf(
            "version" to inputs.properties["version"],
            "mc_range" to inputs.properties["mc_range"]
        )
        expand(props)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}
