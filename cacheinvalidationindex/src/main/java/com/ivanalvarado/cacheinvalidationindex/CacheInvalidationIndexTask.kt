package com.ivanalvarado.cacheinvalidationindex

import com.ivanalvarado.cacheinvalidationindex.domain.usecase.AffectedSubgraphs
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.BuildDagFromDependencyPairs
import com.ivanalvarado.cacheinvalidationindex.domain.usecase.FindDependencyPairs
import org.gradle.api.DefaultTask
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class CacheInvalidationIndexTask @Inject constructor(
    private val findDependencyPairs: FindDependencyPairs,
    private val buildDagFromDependencyPairs: BuildDagFromDependencyPairs,
    private val affectedSubgraphs: AffectedSubgraphs
) : DefaultTask() {

    @get:Input
    abstract val configurationToAnalyze: SetProperty<String>

    @TaskAction
    fun run() {
        val rootProject = project.rootProject
        val configurationsToAnalyze = configurationToAnalyze.get()
        val sampleList = findDependencyPairs(rootProject, configurationsToAnalyze)
        println("Dependency Pairs: $sampleList")
        val dag = buildDagFromDependencyPairs(sampleList)
        println("DAG: $dag")
        val affectedSubgraphs = affectedSubgraphs(dag)
        println("Affected Subgraphs: $affectedSubgraphs")
    }
}
