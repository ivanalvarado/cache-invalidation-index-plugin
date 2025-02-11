package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.Test


class BuildDagFromDependencyPairsImplTest {

    val buildDagFromDependencyPairs = BuildDagFromDependencyPairsImpl()

    @Test
    fun `invoke - given a list of dependency pairs, should return a directed acyclic graph`() {
        // Given
        val dependencyPairs = getDependencyPairs()
        val expected = DirectedAcyclicGraph.createBuilder<String, String>(String::class.java)
            .addEdge("root", "feature", "implementation")
            .addEdge("feature", "featureImpl", "implementation")
            .addEdge("featureImpl", "library", "api")
            .addEdge("library", "testingLibrary", "testImplementation")
            .build()

        // When
        val result = buildDagFromDependencyPairs(dependencyPairs)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    private fun getDependencyPairs(): List<Triple<Project, Project, String>> {
        return listOf(
            Triple(buildProject("root"), buildProject("feature"), "implementation"),
            Triple(buildProject("feature"), buildProject("featureImpl"), "implementation"),
            Triple(buildProject("featureImpl"), buildProject("library"), "api"),
            Triple(buildProject("library"), buildProject("testingLibrary"), "testImplementation")
        )
    }

    private fun buildProject(name: String): Project {
        return ProjectBuilder.builder().withName(name).build()
    }
}
