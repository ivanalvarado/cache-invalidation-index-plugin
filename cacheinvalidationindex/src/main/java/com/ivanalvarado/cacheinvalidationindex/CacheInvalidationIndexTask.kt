package com.ivanalvarado.cacheinvalidationindex

import com.ivanalvarado.cacheinvalidationindex.domain.usecase.AffectedSubgraphs
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.BuildDagFromDependencyPairs
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.CalculateCacheInvalidationIndex
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.FindDependencyPairs
import com.ivanalvarado.cacheinvalidationindex.writer.GraphVizWriter
import org.gradle.api.DefaultTask
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class CacheInvalidationIndexTask @Inject constructor(
    private val findDependencyPairs: FindDependencyPairs,
    private val buildDagFromDependencyPairs: BuildDagFromDependencyPairs,
    private val affectedSubgraphs: AffectedSubgraphs,
    private val calculateCacheInvalidationIndex: CalculateCacheInvalidationIndex,
    private val graphVizWriter: GraphVizWriter
) : DefaultTask() {

    @get:Input
    abstract val configurationToAnalyze: SetProperty<String>

    @TaskAction
    fun run() {
        val rootProject = project.rootProject
        val configurationsToAnalyze = configurationToAnalyze.get()
        val sampleList = findDependencyPairs(rootProject, configurationsToAnalyze)
        val dag = buildDagFromDependencyPairs(sampleList)
        val affectedSubgraphs = affectedSubgraphs(dag)
        val affectedDag = affectedSubgraphs.find { it.node == project.path }!!.affectedDag
        val cacheInvalidationIndex = calculateCacheInvalidationIndex(affectedDag)
        println("cacheInvalidationIndex: $cacheInvalidationIndex")
        graphVizWriter.writeGraph(
            graph = affectedDag,
            file = project.file("build/cacheinvalidationindex/dependency_graph.png")
        )
    }
}
