import org.asciidoctor.gradle.AsciidoctorTask

plugins {
    id("com.gradle.build-scan") version "1.16"
    id("org.gradle.guides.topical") version "0.14.1"
    id("org.gradle.guides.test-jvm-code") version "0.14.1"
}

repositories {
    maven {
        url = uri("https://repo.gradle.org/gradle/libs")
    }
}

dependencies {
    constraints {
        testImplementation("org.codehaus.groovy:groovy-all:2.5.3")
    }
    testImplementation("org.gradle:sample-check:0.6.1")
    testImplementation(gradleTestKit())
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")
}

guide {
    repoPath = "gradle-guides/implementing-gradle-plugins"
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
    if (!System.getenv("CI").isNullOrEmpty()) {
        publishAlways()
        tag("CI")
    }
}

tasks {
    getByName<AsciidoctorTask>("asciidoctor") {
        inputs.dir("samples")
        attributes(
            mapOf("groovy-example-dir" to file("samples/groovy-dsl"),
                  "kotlin-example-dir" to file("samples/kotlin-dsl"))
        )
    }
}
