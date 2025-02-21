package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.AbstractGraph

class CalculateCacheInvalidationIndexImpl : CalculateCacheInvalidationIndex {
    override operator fun invoke(dag: AbstractGraph<String, DependencyEdge>): Int {
        val target = dag.vertexSet().find { dag.outgoingEdgesOf(it).isEmpty() }
            ?: throw IllegalArgumentException("No target module found in DAG")

        val visited = mutableSetOf<String>()
        val stack = ArrayDeque<String>()

        stack.add(target)

        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            for (edge in dag.incomingEdgesOf(current)) {
                if (edge.configuration == "implementation" || edge.configuration == "api") {
                    val source = dag.getEdgeSource(edge)
                    if (visited.add(source)) { // add returns true if the element was not already present
                        stack.add(source)
                    }
                }
            }
        }
        return visited.size
    }
}
