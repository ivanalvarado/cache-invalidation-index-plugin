package com.ivanalvarado.cacheinvalidationindex

import com.ivanalvarado.cacheinvalidationindex.domain.usecase.AffectedSubgraphsImpl
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.BuildDagFromDependencyPairsImpl
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.FindDependencyPairsImpl
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

        val cacheInvalidationIndexTaskProvider = project.tasks.register(
            "cacheInvalidationIndex",
            CacheInvalidationIndexTask::class.java,
            FindDependencyPairsImpl(),
            BuildDagFromDependencyPairsImpl(),
            AffectedSubgraphsImpl()
        )
        cacheInvalidationIndexTaskProvider.configure { task ->
            task.configurationToAnalyze.set(extension.configurationToAnalyze)
            task.group = "Example"
            task.description = "Prints a greeting message."
        }
    }
}
