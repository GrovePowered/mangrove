plugins {
    `maven-publish`
}

val patchesVersion = "1.20.4"

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

val mangroveArtifact = artifacts.add("mangrove", generateMangroveZip)

publishing {
    publications {
        register<MavenPublication>("mangrove") {
            groupId = "com.grovepowered.mangrove"
            artifactId = "mangrove"
            version = patchesVersion

            artifact(mangroveArtifact)
        }
    }

    repositories {
        maven("https://repo.rubygame.net/repository/maven-snapshots/") {
            name = "RubyGame"
            credentials {
                username = "admin"
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}