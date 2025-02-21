package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.Test

class CalculateCacheInvalidationIndexImplTest {

    private val calculateCacheInvalidationIndex = CalculateCacheInvalidationIndexImpl()

    @Test
    fun `invoke - given a directed acyclic graph, returns the cache invalidation index`() {
        // Given
        val dag = getDag()
        val expected = 4

        // When
        val result = calculateCacheInvalidationIndex(dag)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    /**
     * Builds a simple multi-project structure:
     *  ```mermaid
     *  graphTD
     *  root -> feature (implementation)
     *  root -> library (implementation)
     *  feature -> featureImpl (implementation)
     *  featureImpl -> core (api)
     *  library -> core (api)
     *  testingLibrary -> core (testImplementation)
     *  ```
     */
    private fun getDag(): DirectedAcyclicGraph<String, DependencyEdge> {
        return DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
            .addEdge("root", "feature", DependencyEdge("implementation"))
            .addEdge("root", "library", DependencyEdge("implementation"))
            .addEdge("feature", "featureImpl", DependencyEdge("implementation"))
            .addEdge("featureImpl", "core", DependencyEdge("api"))
            .addEdge("library", "core", DependencyEdge("api"))
            .addEdge("testingLibrary", "core", DependencyEdge("testImplementation"))
            .build()
    }
}
