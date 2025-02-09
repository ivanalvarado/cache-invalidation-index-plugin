package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Register a custom task named 'hello'
        project.tasks.register("hello", HelloTask::class.java) {
            it.group = "Example"
            it.description = "Prints a greeting message."
        }
    }
}
