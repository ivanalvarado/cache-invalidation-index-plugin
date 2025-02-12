package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.AffectedSubgraphDetails
import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.DirectedAcyclicGraph

interface AffectedSubgraphs {
    operator fun invoke(graph: DirectedAcyclicGraph<String, DependencyEdge>): List<AffectedSubgraphDetails>
}
