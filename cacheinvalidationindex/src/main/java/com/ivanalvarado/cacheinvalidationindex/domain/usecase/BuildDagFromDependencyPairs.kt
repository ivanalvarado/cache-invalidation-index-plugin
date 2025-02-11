package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import org.gradle.api.Project
import org.jgrapht.graph.DirectedAcyclicGraph

interface BuildDagFromDependencyPairs {
    operator fun invoke(dependencyPairs: List<Triple<Project, Project, String>>): DirectedAcyclicGraph<String, String>
}
