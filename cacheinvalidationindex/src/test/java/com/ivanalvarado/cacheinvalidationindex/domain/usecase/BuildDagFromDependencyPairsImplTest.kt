package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.jgrapht.Graph
import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.Test


class BuildDagFromDependencyPairsImplTest {

    val buildDagFromDependencyPairs = BuildDagFromDependencyPairsImpl()

    @Test
    fun `invoke - given a list of dependency pairs, should return a directed acyclic graph`() {
        // Given
        val dependencyPairs = getDependencyPairs()
        val expected = DirectedAcyclicGraph.createBuilder<String, DependencyEdge>(DependencyEdge::class.java)
            .addEdge(":", ":feature", DependencyEdge("implementation"))
            .addEdge(":feature", ":featureImpl", DependencyEdge("implementation"))
            .addEdge(":featureImpl", ":library", DependencyEdge("api"))
            .addEdge(":library", ":testingLibrary", DependencyEdge("testImplementation"))
            .build()

        // When
        val result = buildDagFromDependencyPairs(dependencyPairs)

        // Then
        assertThat(result.vertexSet()).isEqualTo(expected.vertexSet())
        assertThat(result.toEdgeTriples()).isEqualTo(expected.toEdgeTriples())
    }

    private fun getDependencyPairs(): List<Triple<Project, Project, String>> {
        val root = ProjectBuilder.builder().withName("root").build()
        val feature = buildProjectWithParent("feature", root)
        val featureImpl = buildProjectWithParent("featureImpl", root)
        val library = buildProjectWithParent("library", root)
        val testingLibrary = buildProjectWithParent("testingLibrary", root)
        return listOf(
            Triple(root, feature, "implementation"),
            Triple(feature, featureImpl, "implementation"),
            Triple(featureImpl, library, "api"),
            Triple(library, testingLibrary, "testImplementation")
        )
    }

    private fun buildProjectWithParent(name: String, parent: Project): Project {
        return ProjectBuilder.builder().withName(name).withParent(parent).build()
    }

    private fun Graph<String, DependencyEdge>.toEdgeTriples(): Set<Triple<String, String, String>> {
        return this.edgeSet().map { edge ->
            Triple(this.getEdgeSource(edge), this.getEdgeTarget(edge), edge.configuration)
        }.toSet()
    }
}
