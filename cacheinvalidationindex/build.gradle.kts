plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm")
}

group = "com.ivanalvarado"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("myPlugin") {
            id = "com.ivanalvarado.cacheinvalidationindex"
            implementationClass = "com.ivanalvarado.cacheinvalidationindex.CacheInvalidationIndexPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
