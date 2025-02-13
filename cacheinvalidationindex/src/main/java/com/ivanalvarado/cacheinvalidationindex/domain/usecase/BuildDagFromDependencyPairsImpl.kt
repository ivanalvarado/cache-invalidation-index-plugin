package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.gradle.api.Project
import org.jgrapht.graph.DirectedAcyclicGraph

class BuildDagFromDependencyPairsImpl : BuildDagFromDependencyPairs {

    override fun invoke(
        dependencyPairs: List<Triple<Project, Project, String>>
    ): DirectedAcyclicGraph<String, DependencyEdge> {
        val dag = DirectedAcyclicGraph<String, DependencyEdge>(DependencyEdge::class.java)

        dependencyPairs.forEach { (project, dependency, configuration) ->
            dag.addVertex(project.path)
            dag.addVertex(dependency.path)
            dag.addEdge(project.path, dependency.path, DependencyEdge(configuration))
        }

        return dag
    }
}
