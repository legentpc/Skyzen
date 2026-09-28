import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
    id("dev.detekt")
}

val versionedLibs = the<VersionCatalogsExtension>()
    .find("versionedLibs${sc.current.project.replace(".", "")}")
    .get()

fun VersionCatalog.lib(name: String) = findLibrary(name).get()
fun VersionCatalog.ver(name: String) = findVersion(name).get()

fun DependencyHandlerScope.includeImplementation(dependencyNotation: Provider<*>) {
    include(dependencyNotation)
    implementation(dependencyNotation)
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

    maven("https://maven.terraformersmc.com/releases") {
        content {
            includeGroup("com.terraformersmc")
        }
    }
}

dependencies {
    minecraft(versionedLibs.lib("minecraft"))

    implementation(libs.fabric.loader)
    implementation(libs.fabric.language.kotlin)
    implementation(versionedLibs.lib("fabric-api"))

    includeImplementation(libs.lattice)
    compileOnly(versionedLibs.lib("modmenu"))
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
        javaParameters.set(true)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

detekt {
    toolVersion = "2.0.0-alpha.6"
    source.setFrom(rootProject.file("src/main/fabric"))
    config.setFrom(rootProject.file("detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
    ignoreFailures = false
    basePath.set(rootProject.projectDir)
}
