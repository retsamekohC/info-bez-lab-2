plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.lizonbka"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(group = "org.apache.poi", name = "poi-ooxml", version = "4.1.1")
    implementation(group = "org.apache.xmlbeans", name = "xmlbeans", version = "3.1.0")
    implementation("javax.xml.stream:stax-api:1.0")
    implementation("com.fasterxml:aalto-xml:1.2.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}