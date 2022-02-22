import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "1.6.10"
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

tasks.withType<Test>().configureEach {
    outputs.upToDateWhen { false }
    testLogging.showStandardStreams = true
    useJUnitPlatform()
    systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
    // finalizedBy("allureReport")
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("io.kotest:kotest-runner-junit5:5.1.0")
    implementation("io.kotest:kotest-assertions-core:5.1.0")
    implementation("io.kotest:kotest-property:5.1.0")
    implementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")
    implementation("io.kotest.extensions:kotest-extensions-allure:1.0.3")
    implementation("io.kotest:kotest-extensions-junitxml:5.1.0")
    implementation("io.kotest:kotest-extensions-htmlreporter-jvm:5.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    implementation("com.epam.reportportal:agent-java-junit5:5.1.1")
    implementation("com.epam.reportportal:logger-java-logback:5.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
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
