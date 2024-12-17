plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "com.alttd.event"
version = System.getenv("BUILD_NUMBER") ?: "1.0-SNAPSHOT"
description = "Altitude's Christmas hide and seek plugin"

apply<JavaLibraryPlugin>()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    jar {
        enabled = false
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filesMatching("plugin.yml") {
            expand(Pair("version", project.version))
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}.jar")
        manifest {
            attributes("Main-Class" to "WinterHideAndSeek")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    compileOnly("com.alttd:Galaxy-API:1.21-R0.1-SNAPSHOT") {
        isChanging = true
    }

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}