plugins {
    java
    `maven-publish`
}

val patchesVersion = "1.20.4-SNAPSHOT"

val generateMangroveZip by tasks.registering(Zip::class) {
    group = "mangrove"
    description = "Creates the Mangrove artifact for publishing."

    into("patches") {
        from(layout.projectDirectory.dir("patches"))
    }

    archiveBaseName.set("mangrove")
    archiveVersion.set(patchesVersion)
    archiveExtension.set("zip")
}

val archive = artifacts.archives(generateMangroveZip)

publishing {
    publications {
        register<MavenPublication>("mangrove") {
            groupId = "com.grovepowered.mangrove"
            artifactId = "mangrove"
            version = patchesVersion

            artifact(archive)
        }
    }

    val useSnapshots = System.getenv("USE_SNAPSHOTS")?.toBoolean() ?: true

    repositories {
        if (useSnapshots) {
            maven {
                url = uri("https://repo.rubygame.net/repository/maven-snapshots/")
                credentials {
                    username = "admin"
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        } else {
            maven {
                url = uri("https://repo.rubygame.net/repository/maven-releases/")
                credentials {
                    username = "admin"
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        }
    }
}