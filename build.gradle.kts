import org.apache.tools.ant.filters.*
import org.asciidoctor.gradle.AsciidoctorTask

plugins {
    id("com.gradle.build-scan") version "1.14"
    id("org.gradle.guides.getting-started") version "0.13.3"
    id("org.gradle.guides.test-jvm-code") version "0.13.3"
}

configure<org.gradle.guides.GuidesExtension> {
    setRepoPath("gradle-guides/creating-build-scans")
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
    val preProcessSamples = "preProcessSamples"(Copy::class) {
        into("$buildDir/samples")
        from("samples")
        dependsOn("configurePreProcessSamples")
    }

    "configurePreProcessSamples" {
        doLast {
            val tokens = mapOf("scanPluginVersion" to resolveLatestBuildScanPluginVersion())
            preProcessSamples.inputs.properties(tokens)
            preProcessSamples.filter<ReplaceTokens>("tokens" to tokens)
        }
    }
    val asciidoctor by getting(AsciidoctorTask::class) {
        dependsOn("preProcessSamples")
        attributes.putAll(mapOf(
            "samplescodedir" to project.file("build/samples/code").absolutePath
        ))
    }
    val test by getting(Test::class) {
        dependsOn("preProcessSamples")
        systemProperty("samplesDir", "$buildDir/samples")
    }
}
