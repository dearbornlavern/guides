plugins {
    java
    application
}

repositories {
    jcenter() // <1>
}

dependencies {
    implementation("com.google.guava:guava:26.0-jre") // <2>

    testImplementation("junit:junit:4.12") // <3>
}

application {
    mainClassName = "demo.App" // <4>
}
