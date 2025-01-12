val awsSdkVersion: String by ext
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
val postgresqlVersion: String by ext
val postgresR2dbcDriverVersion: String by ext
val resilience4jVersion: String by ext
val springCloudVersion: String by ext
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        mavenBom("software.amazon.awssdk:bom:$awsSdkVersion")
        mavenBom("io.github.resilience4j:resilience4j-bom:$resilience4jVersion")
        mavenBom("org.testcontainers:testcontainers-bom:$testContainerVersion")
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

        dependency("org.mock-server:mockserver-client-java:$mockServerClientJavaVersions")
    }
}

ext["kotlin.version"] = kotlinVersion
ext["kotlin-coroutines.version"] = kotlinxCoroutinesVersion
ext["log4j2.version"] = log4j2Version
ext["netty.version"] = nettyVersion
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

    implementation("software.amazon.awssdk:s3")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql") {
        exclude(group = "io.projectreactor.netty", module = "reactor-netty-http-brave")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

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
}

ktlint {
    debug.set(false)
}

springBoot {
    buildInfo()
}
