import java.io.IOException
import java.util.Properties

val awsSdkVersion: String by ext
val flywayVersion: String by ext
val jacksonVersion: String by ext
val kotestVersion: String by ext
val kotlinVersion: String by ext
val kotlinxCoroutinesVersion: String by ext
val kotlinLoggingVersion: String by ext
val lmaxDisruptorVersion: String by ext
val log4j2Version: String by ext
val mockkVersion: String by ext
val mockServerClientJavaVersions: String by ext
val nettyVersion: String by ext
val natsVersion: String by ext
val postgresqlVersion: String by ext
val postgresR2dbcDriverVersion: String by ext
val resilience4jVersion: String by ext
val springCloudVersion: String by ext
val springCloudAwsVersion: String by ext
val testContainerVersion: String by ext

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    jacoco
}

group = "io.github.soat7.videomanager"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val props = Properties()
try {
    props.load(file("$projectDir/.env").inputStream())
} catch (e: IOException) {
    println(e.message)
}

apply {
    plugin("kotlin")
    plugin("kotlin-spring")
    plugin("io.spring.dependency-management")
    plugin("org.jlleitschuh.gradle.ktlint")
    plugin("jacoco")
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "io.arrow-kt", module = "arrow-core-extensions")
        exclude(group = "io.projectreactor.netty", module = "reactor-netty-http-brave")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        mavenBom("io.github.resilience4j:resilience4j-bom:$resilience4jVersion")
        mavenBom("org.testcontainers:testcontainers-bom:$testContainerVersion")
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:$springCloudAwsVersion")
    }

    dependencies {
        dependency("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
        dependency("com.lmax:disruptor:$lmaxDisruptorVersion")

        dependency("io.kotest:kotest-runner-junit5:$kotestVersion")
        dependency("io.kotest:kotest-assertions-core:$kotestVersion")
        dependency("io.kotest:kotest-property:$kotestVersion")
        dependency("io.mockk:mockk:$mockkVersion")
        dependency("org.postgresql:postgresql:$postgresqlVersion")
        dependency("io.r2dbc:r2dbc-postgresql:$postgresR2dbcDriverVersion")
        dependency("com.lmax:disruptor:$lmaxDisruptorVersion")
        dependency("org.flywaydb:flyway-core:$flywayVersion")
        dependency("org.mock-server:mockserver-client-java:$mockServerClientJavaVersions")
        dependency("com.amazonaws:aws-java-sdk-s3:$awsSdkVersion")
    }
}

ext["kotlin.version"] = kotlinVersion
ext["kotlin-coroutines.version"] = kotlinxCoroutinesVersion
ext["log4j2.version"] = log4j2Version
ext["jackson-bom.version"] = jacksonVersion

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging")
    implementation("com.lmax:disruptor")

    // Spring Framework
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation") {
        exclude(group = "org.apache.tomcat.embed", module = "tomcat-embed-el")
    }

    implementation("io.nats:nats-spring-boot-starter:0.6.0-3.1")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Circuit breaker
    implementation("io.github.resilience4j:resilience4j-spring-boot3")
    implementation("io.github.resilience4j:resilience4j-reactor")
    implementation("io.github.resilience4j:resilience4j-kotlin")
    implementation("io.github.resilience4j:resilience4j-retry")
    implementation("io.github.resilience4j:resilience4j-micrometer")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql") {
        exclude(group = "io.projectreactor.netty", module = "reactor-netty-http-brave")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.flywaydb:flyway-core")

    implementation("com.amazonaws:aws-java-sdk-s3")
    implementation("commons-io:commons-io:2.14.0")

    // Testing
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:mockserver")
    testImplementation("org.mock-server:mockserver-client-java")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug")
    testImplementation("io.mockk:mockk")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks {
    jar {
        enabled = false
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
            )
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    jacocoTestReport {
        reports {
            xml.required = true
            csv.required = true
            html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test/html"))
            xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacoco.xml"))
            csv.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacoco.csv"))
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it).apply {
                            exclude("**/mapper/**")
                            exclude("**/model/**")
                            exclude("**/api/**")
                            exclude("**/enum/**")
                            exclude("**/config/**")
                            exclude("**/common/**")
                            exclude("**/exception/*")
                            exclude("*/Application*")
                        }
                    }
                )
            )
        }
        dependsOn(withType<Test>())
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = 0.80.toBigDecimal()
                }
            }
        }
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}

ktlint {
    debug.set(false)
    this.coloredOutput.set(true)
    this.outputToConsole.set(true)
}

springBoot {
    buildInfo()
}
