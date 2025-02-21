package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.AbstractGraph

interface CalculateCacheInvalidationIndex {
    operator fun invoke(dag: AbstractGraph<String, DependencyEdge>): Int
}
