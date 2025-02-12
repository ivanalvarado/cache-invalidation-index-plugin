package com.ivanalvarado.cacheinvalidationindex.domain.model

import org.jgrapht.graph.AbstractGraph

data class AffectedSubgraphDetails(
    val node: String,
    val affectedDag: AbstractGraph<String, DependencyEdge>
)
