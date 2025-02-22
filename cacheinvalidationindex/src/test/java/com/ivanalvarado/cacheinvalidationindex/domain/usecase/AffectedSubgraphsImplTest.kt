package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ivanalvarado.cacheinvalidationindex.domain.model.AffectedSubgraphDetails
import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.Test

class AffectedSubgraphsImplTest {
    val affectedSubgraphs = AffectedSubgraphsImpl()

    @Test
    fun `invoke - given a directed acyclic graph, should return a list of sets of affected nodes`() {
        // Given
        val dag = getDag()
        val expected =
            listOf(
                AffectedSubgraphDetails(
                    "root",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addVertex("root")
                        .build(),
                ),
                AffectedSubgraphDetails(
                    "feature",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addEdge("root", "feature", DependencyEdge("implementation"))
                        .build(),
                ),
                AffectedSubgraphDetails(
                    "featureImpl",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addEdge("feature", "featureImpl", DependencyEdge("api"))
                        .addEdge("root", "feature", DependencyEdge("implementation"))
                        .build(),
                ),
                AffectedSubgraphDetails(
                    "core",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addEdge("featureImpl", "core", DependencyEdge("api"))
                        .addEdge("feature", "featureImpl", DependencyEdge("api"))
                        .addEdge("root", "feature", DependencyEdge("implementation"))
                        .build(),
                ),
                AffectedSubgraphDetails(
                    "library",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addEdge("featureImpl", "library", DependencyEdge("api"))
                        .addEdge("feature", "featureImpl", DependencyEdge("api"))
                        .addEdge("root", "feature", DependencyEdge("implementation"))
                        .build(),
                ),
                AffectedSubgraphDetails(
                    "testingLibrary",
                    DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
                        .addVertex("testingLibrary")
                        .build(),
                ),
            )

        // When
        val result = affectedSubgraphs(dag)

        // Then
        assertThat(result[0].node).isEqualTo(expected[0].node)
        assertThat(result[1].node).isEqualTo(expected[1].node)
        assertThat(result[2].node).isEqualTo(expected[2].node)
        assertThat(result[3].node).isEqualTo(expected[3].node)
        assertThat(result[4].node).isEqualTo(expected[4].node)
        assertThat(result[5].node).isEqualTo(expected[5].node)
        assertThat(result[0].affectedDag.vertexSet()).isEqualTo(expected[0].affectedDag.vertexSet())
        assertThat(result[1].affectedDag.vertexSet()).isEqualTo(expected[1].affectedDag.vertexSet())
        assertThat(result[2].affectedDag.vertexSet()).isEqualTo(expected[2].affectedDag.vertexSet())
        assertThat(result[3].affectedDag.vertexSet()).isEqualTo(expected[3].affectedDag.vertexSet())
        assertThat(result[4].affectedDag.vertexSet()).isEqualTo(expected[4].affectedDag.vertexSet())
        assertThat(result[5].affectedDag.vertexSet()).isEqualTo(expected[5].affectedDag.vertexSet())
        assertThat(result[0].affectedDag.edgeSet()).isEqualTo(expected[0].affectedDag.edgeSet())
        assertThat(result[1].affectedDag.edgeSet().toString()).isEqualTo(expected[1].affectedDag.edgeSet().toString())
        assertThat(result[2].affectedDag.edgeSet().toString()).isEqualTo(expected[2].affectedDag.edgeSet().toString())
        assertThat(result[3].affectedDag.edgeSet().toString()).isEqualTo(expected[3].affectedDag.edgeSet().toString())
        assertThat(result[4].affectedDag.edgeSet().toString()).isEqualTo(expected[4].affectedDag.edgeSet().toString())
        assertThat(result[5].affectedDag.edgeSet().toString()).isEqualTo(expected[5].affectedDag.edgeSet().toString())
    }

    /**
     * Builds a simple multi-project structure:
     *  ```mermaid
     *  graphTD
     *  root -> feature (implementation)
     *  root -> featureImpl (implementation)
     *  feature -> featureImpl (api)
     *  featureImpl -> library (api)
     *  featureImpl -> testingLibrary (testImplementation)
     *  featureImpl -> core (api)
     *  library -> testingLibrary (testImplementation)
     *  ```
     */
    private fun getDag(): DirectedAcyclicGraph<String, DependencyEdge> {
        return DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
            .addEdge("root", "feature", DependencyEdge("implementation"))
            .addEdge("feature", "featureImpl", DependencyEdge("api"))
            .addEdge("featureImpl", "core", DependencyEdge("api"))
            .addEdge("featureImpl", "library", DependencyEdge("api"))
            .addEdge("featureImpl", "testingLibrary", DependencyEdge("testImplementation"))
            .addEdge("library", "testingLibrary", DependencyEdge("testImplementation"))
            .build()
    }
}
