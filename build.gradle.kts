@file:Suppress("UnstableApiUsage")

import org.gradle.internal.fingerprint.classpath.impl.ClasspathFingerprintingStrategy.compileClasspath
import org.gradle.kotlin.dsl.compileClasspath


plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("info.solidsoft.pitest") version "1.19.0-rc.1"
	jacoco
}

group = "fr.ibaraki"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val testIntegrationImplementation: Configuration by configurations.creating {
	extendsFrom(configurations.implementation.get(), configurations.testImplementation.get())
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest:kotest-property:5.9.1")
	testImplementation("io.mockk:mockk:1.14.4")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	pitest("org.pitest:pitest-junit5-plugin:1.2.1")

	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}

}


testing {
	suites {
		val testIntegration by registering(JvmTestSuite::class) {

			sources {
				kotlin {
					setSrcDirs(listOf("src/testIntegration/kotlin"))
				}
				resources {
					setSrcDirs(listOf("src/testIntegration/resources"))
				}

				compileClasspath += sourceSets.main.get().output
				runtimeClasspath += sourceSets.main.get().output
			}

		}
	}
}


kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.required = true
		csv.required = false
	}
}
jacoco {
	toolVersion = "0.8.13"
}

pitest {
	threads = 4
	outputFormats = setOf("XML", "HTML")
	timestampedReports = false
	targetClasses = setOf("fr.ibaraki.books.*")
	targetTests = setOf("fr.ibaraki.books.*")
	excludedClasses = setOf("fr.ibaraki.books.BooksApplication*")
}