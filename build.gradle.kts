plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.allopen") version "1.8.22"
    kotlin("plugin.jpa") version "1.9.23"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-narayana-jta")
    implementation("io.quarkus:quarkus-jdbc-h2")
    implementation("io.quarkus:quarkus-jdbc-mysql")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-smallrye-jwt-build")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    implementation("io.quarkus:quarkus-config-yaml")
    testImplementation("io.rest-assured:rest-assured")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.1")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.quarkus:quarkus-vertx")
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-web-client")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("jakarta.mail:jakarta.mail-api:2.1.2")
    implementation("io.quarkus:quarkus-qute")
    implementation("io.quarkus:quarkus-mailer")
    implementation("io.quarkus:quarkus-resteasy-qute")

    testImplementation("io.quarkus:quarkus-junit5")
}

group = "me.ronan"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
