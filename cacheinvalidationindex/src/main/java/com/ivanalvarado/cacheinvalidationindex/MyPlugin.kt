package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Register a custom task named 'hello'
        project.tasks.register("cacheInvalidationIndex", CacheInvalidationIndexTask::class.java) {
            it.group = "Example"
            it.description = "Prints a greeting message."
        }
    }
}
