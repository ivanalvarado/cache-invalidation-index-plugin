package com.ivanalvarado.cacheinvalidationindex

import com.ivanalvarado.cacheinvalidationindex.domain.usecase.AffectedSubgraphsImpl
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.BuildDagFromDependencyPairsImpl
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.CalculateCacheInvalidationIndexImpl
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.FindDependencyPairsImpl
import com.ivanalvarado.cacheinvalidationindex.writer.CacheInvalidationIndexWriter
import com.ivanalvarado.cacheinvalidationindex.writer.GraphVizWriter
import org.gradle.api.Plugin
import org.gradle.api.Project

class CacheInvalidationIndexPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(
                CacheInvalidationIndexExtension::class.java,
                "cacheInvalidationIndex",
                CacheInvalidationIndexExtension::class.java,
            )

        val cacheInvalidationIndexTaskProvider =
            project.tasks.register(
                "cacheInvalidationIndex",
                CacheInvalidationIndexTask::class.java,
                FindDependencyPairsImpl(),
                BuildDagFromDependencyPairsImpl(),
                AffectedSubgraphsImpl(),
                CalculateCacheInvalidationIndexImpl(),
                GraphVizWriter(),
                CacheInvalidationIndexWriter(),
            )
        cacheInvalidationIndexTaskProvider.configure { task ->
            task.configurationToAnalyze.set(extension.configurationToAnalyze)
            task.group = "Example"
            task.description = "Prints a greeting message."
        }
    }
}
