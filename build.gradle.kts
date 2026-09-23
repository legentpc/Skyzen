import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.zip.ZipFile

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

val generatedAwDir = layout.buildDirectory.dir("generated/accesswidener").get().asFile
val moulconfigAccessWidener = File(generatedAwDir, "moulconfig.accesswidener")

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
        resources {
            srcDir(generatedAwDir)
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

// MoulConfig ships an access widener that its GUI renderer needs at runtime.
// Shading drops the nested fabric.mod.json that used to declare it, so pull
// the file out of the jar once and expose it to Loom and the packed resources.
if (!moulconfigAccessWidener.exists()) {
    val moulconfigJar = shadowImpl.resolve().first { it.name.startsWith("modern-") }
    generatedAwDir.mkdirs()
    ZipFile(moulconfigJar).use { zip ->
        val entry = zip.getEntry("moulconfig.accesswidener")
            ?: error("moulconfig.accesswidener not found in ${moulconfigJar.name}")
        zip.getInputStream(entry).use { input ->
            moulconfigAccessWidener.outputStream().use { input.copyTo(it) }
        }
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

loom {
    accessWidenerPath.set(moulconfigAccessWidener)
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
