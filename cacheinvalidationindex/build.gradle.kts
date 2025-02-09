plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm")
}

group = "com.ivanalvarado"
version = "0.0.1"

gradlePlugin {
    // Define one or more plugins
    plugins {
        create("myPlugin") {
            id = "com.ivanalvarado.cacheinvalidationindex"          // The plugin ID used in build scripts
            implementationClass = "com.ivanalvarado.cacheinvalidationindex.MyPlugin" // Fully-qualified plugin implementation class
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
