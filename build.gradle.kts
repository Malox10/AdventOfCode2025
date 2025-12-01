plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.github.malox10"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}