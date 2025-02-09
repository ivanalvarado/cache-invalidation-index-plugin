package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.configuration.problems.taskPathFrom

class CacheInvalidationIndexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            CacheInvalidationIndexExtension::class.java,
            "cacheInvalidationIndex",
            CacheInvalidationIndexExtension::class.java
        )

        project.tasks.register("cacheInvalidationIndex", CacheInvalidationIndexTask::class.java) { task ->
            task.configurationToAnalyze.set(extension.configurationToAnalyze)
            task.group = "Example"
            task.description = "Prints a greeting message."
        }
    }
}
