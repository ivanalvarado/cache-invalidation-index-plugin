package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.gradle.api.Project
import org.jgrapht.graph.DirectedAcyclicGraph

interface BuildDagFromDependencyPairs {
    operator fun invoke(dependencyPairs: List<Triple<Project, Project, String>>): DirectedAcyclicGraph<String, DependencyEdge>
}
