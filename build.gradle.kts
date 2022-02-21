import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("io.qameta.allure") version "2.9.6"
    id("com.diffplug.spotless") version "6.2.2"
}

group = "com.onfleet"
version = "0.0.2"

tasks.wrapper {
    version = "7.4"
    distributionType = DistributionType.ALL
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
        ktlint()
    }
}

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    outputs.upToDateWhen { false }
    // finalizedBy("allureReport")
}

kotlin {
    jvm {
    }
    sourceSets {
        commonMain {
            kotlin
            kotlin.srcDirs("src/commonMain")
            resources.srcDirs("commonMain/resources")
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            }
        }
        commonTest {
            kotlin.srcDirs("src/commonTest")
            resources.srcDirs("commonTest")
            dependencies {
                implementation("io.kotest:kotest-assertions-core:5.1.0")
            }
        }
        val jvmMain by getting {
            kotlin.srcDirs("src/jvmMain")
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
        val jvmTest by getting {
            kotlin.srcDirs("src/jvmTest")
            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//                implementation(kotlin("test-junit"))

                implementation("io.kotest:kotest-runner-junit5:5.1.0")
                implementation("io.kotest:kotest-assertions-core:5.1.0")
                implementation("io.kotest:kotest-property:5.1.0")
                implementation("io.kotest:kotest-extensions-junitxml:5.1.0")
                implementation("io.kotest:kotest-extensions-htmlreporter-jvm:5.1.0")
                implementation("io.kotest.extensions:kotest-extensions-allure:1.0.1")

                implementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")

                implementation("com.epam.reportportal:agent-java-junit5:5.1.4")
                implementation("com.epam.reportportal:logger-java-log4j:5.1.4")
                implementation("org.apache.logging.log4j:log4j-api:2.17.1")
                implementation("org.apache.logging.log4j:log4j-core:2.17.1")

            }

            tasks.withType<Test> {
                useJUnitPlatform()
                systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
            }

            allure {
                adapter {
                    //allureJavaVersion.set("2.15.0")
                    //aspectjVersion.set("1.9.5")
                    autoconfigure.set(true)
                    autoconfigureListeners.set(true)
                    aspectjWeaver.set(true)
                }
            }

        }
    }
}
