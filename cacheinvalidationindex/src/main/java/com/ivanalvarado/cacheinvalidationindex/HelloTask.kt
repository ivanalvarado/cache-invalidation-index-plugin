package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class HelloTask : DefaultTask() {
    @TaskAction
    fun greet() {
        println("Hello from my custom Gradle plugin!")
    }
}
