package com.ivanalvarado.cacheinvalidationindex.domain.model

import org.jgrapht.graph.DefaultEdge

class DependencyEdge(val configuration: String) : DefaultEdge() {
    override fun toString(): String {
        return "($source : $target : $configuration)"
    }
}
