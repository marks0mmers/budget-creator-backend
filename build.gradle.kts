import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
	id("org.springframework.boot") version "2.4.0"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
}

group = "com.marks0mmers"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = URI.create("https://repo.maven.apache.org/maven2")
	}
}

dependencies {
	implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot", "spring-boot-starter-security")
	implementation("org.springframework.boot", "spring-boot-starter-webflux")
	implementation("io.jsonwebtoken", "jjwt", "0.9.1")
	implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
	implementation("io.projectreactor.kotlin", "reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin", "kotlin-reflect")
	implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot", "spring-boot-starter-test")
	testImplementation("io.projectreactor", "reactor-test")
	testImplementation("org.springframework.security", "spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
