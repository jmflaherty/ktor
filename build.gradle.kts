import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val kotlin_version: String by project
val kotlinx_coroutines: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("multiplatform") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

group = "com.onfleet"
version = "0.0.1"

tasks.wrapper {
    version = "7.0"
    distributionType = DistributionType.ALL
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
}

kotlin {
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }
    jvm {
    }
    ios{
        binaries {
            framework {
                baseName = "commonMain"
            }
        }
    }
    sourceSets {
        commonMain {
            kotlin.srcDirs("src/commonMain")
            resources.srcDirs("commonMain/resources")
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("ch.qos.logback:logback-classic:$logback_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines")
            }
        }
        commonTest {
            kotlin.srcDirs("src/commonTest")
            resources.srcDirs("commonTest")
            dependencies { }
        }
        val jvmMain by getting {
            kotlin.srcDirs("src/jvmMain")
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktor_version")
            }
        }
        val jvmTest by getting {
            kotlin.srcDirs("src/jvmTest")
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinx_coroutines")
            }
        }
        val jsMain by getting {
            kotlin.srcDirs("src/jsMain")
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.ktor:ktor-client-js:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinx_coroutines")
            }
        }
        val jsTest by getting {
            kotlin.srcDirs("src/jsTest")
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-js")
            }
        }
        val iosMain by getting {
            kotlin.srcDirs("src/iosMain")
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktor_version")
            }
        }
        val iosTest by getting {
            kotlin.srcDirs("src/iosTest")
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)
