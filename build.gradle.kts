import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
}

val versionedLibs = the<VersionCatalogsExtension>()
    .find("versionedLibs${sc.current.project.replace(".", "")}")
    .get()

fun VersionCatalog.lib(name: String) = findLibrary(name).get()
fun VersionCatalog.ver(name: String) = findVersion(name).get()

val shadowImpl = configurations.create("shadowImpl") {
    configurations.implementation.get().extendsFrom(this)
}

version = "${providers.gradleProperty("mod_version").get()}-mc${sc.current.project}"
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

    shadowImpl(versionedLibs.lib("moulconfig")) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
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

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = listOf(shadowImpl)
    exclude("META-INF/versions/**")
    exclude("META-INF/*.kotlin_module")
    mergeServiceFiles()
    relocate("io.github.notenoughupdates.moulconfig", "at.legentpc.skyzen.deps.moulconfig")
}

tasks.jar {
    archiveClassifier.set("nodeps")
    destinationDirectory.set(layout.buildDirectory.dir("devlibs"))
}

tasks.assemble.get().dependsOn(tasks.shadowJar)
