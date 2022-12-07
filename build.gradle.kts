import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("maven-publish")
    id("java")
}



repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing{
    publications {
        create<MavenPublication>("maven"){
            groupId = "de.rehatech"
            artifactId ="homeekt"
            version = "0.0.1"
            from(components["java"])
        }
    }
}