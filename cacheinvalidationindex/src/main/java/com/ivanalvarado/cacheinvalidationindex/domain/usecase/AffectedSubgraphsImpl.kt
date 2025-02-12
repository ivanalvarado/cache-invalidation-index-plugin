package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.AffectedSubgraphDetails
import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.DirectedAcyclicGraph

class AffectedSubgraphsImpl : AffectedSubgraphs {
    override fun invoke(
        graph: DirectedAcyclicGraph<String, DependencyEdge>
    ): List<AffectedSubgraphDetails> {
        return graph.vertexSet().map { node ->
            val subgraph = buildSubgraphForNode(graph, node)
            AffectedSubgraphDetails(node, subgraph)
        }
    }

    private fun buildSubgraphForNode(
        graph: DirectedAcyclicGraph<String, DependencyEdge>,
        node: String
    ): DirectedAcyclicGraph<String, DependencyEdge> {
        val nodesToInclude = mutableSetOf<String>()
        val edgesToInclude = mutableSetOf<DependencyEdge>()

        fun traverse(current: String) {
            // Look at every incoming edge from a source node to 'current'
            for (edge in graph.incomingEdgesOf(current)) {
                val source = graph.getEdgeSource(edge)
                when (edge.configuration) {
                    "api" -> {
                        // If we see an API dependency, include the source and keep going.
                        if (nodesToInclude.add(source)) {
                            edgesToInclude.add(edge)
                            traverse(source)
                        }
                    }
                    "implementation" -> {
                        // For implementation, include the source but do not continue further.
                        nodesToInclude.add(source)
                        edgesToInclude.add(edge)
                    }
                    // Ignore other dependency types (e.g., testImplementation)
                }
            }
        }

        nodesToInclude.add(node)
        traverse(node)

        val builder = DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(
            DependencyEdge::class.java
        )
        nodesToInclude.forEach { builder.addVertex(it) }
        edgesToInclude.forEach { edge ->
            val source = graph.getEdgeSource(edge)
            val target = graph.getEdgeTarget(edge)
            // Only add the edge if both vertices are in the subgraph.
            if (nodesToInclude.contains(source) && nodesToInclude.contains(target)) {
                builder.addEdge(source, target, edge)
            }
        }
        return builder.build()
    }
}

