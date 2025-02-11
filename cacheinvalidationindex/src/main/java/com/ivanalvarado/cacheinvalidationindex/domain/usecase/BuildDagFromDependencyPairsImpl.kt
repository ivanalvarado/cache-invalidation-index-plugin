package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import org.gradle.api.Project
import org.jgrapht.graph.DirectedAcyclicGraph

class BuildDagFromDependencyPairsImpl : BuildDagFromDependencyPairs {

    override fun invoke(
        dependencyPairs: List<Triple<Project, Project, String>>
    ): DirectedAcyclicGraph<String, String> {
        val dag = DirectedAcyclicGraph<String, String>(String::class.java)

        dependencyPairs.forEach { (project, dependency, configuration) ->
            dag.addVertex(project.name)
            dag.addVertex(dependency.name)
            dag.addEdge(project.name, dependency.name, configuration)
        }

        return dag
    }
}
