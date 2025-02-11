package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class FindDependencyPairsImplTest {

    val findDependencyPairs = FindDependencyPairsImpl()

    @Test
    fun `invoke - given a project with specified configurations, should return a list of dependency pairs with the specified configurations`() {
        // Given
        val rootProject = buildProject()
        val featureProject = rootProject.subprojects.first()
        val featureImplProject = featureProject.subprojects.first()
        val libraryProject = featureImplProject.subprojects.first()
        val configurationsToAnalyse = setOf("implementation", "api")
        val expected = listOf(
            Triple(
                featureProject,
                featureImplProject,
                "implementation"
            ),
            Triple(
                featureImplProject,
                libraryProject,
                "api"
            )
        )

        // When
        val result = findDependencyPairs(rootProject, configurationsToAnalyse)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    /**
     * Builds a simple multi-project structure:
     * - rootProject
     *   - featureProject
     *     - featureImplProject
     *       - libraryProject
     *         - testingLibraryProject
     *
     * The 'java-library' plugin is applied so that the required plugins configurations exists,
     * and inter-project dependencies are set up as follows:
     * - rootProject -> featureProject (implementation)
     * - featureProject -> featureImplProject (implementation)
     * - featureImplProject -> libraryProject (api)
     * - libraryProject -> testingLibraryProject (testImplementation)
     */
    private fun buildProject(): Project {
        val rootProject = ProjectBuilder.builder().withName("root").build()
        val featureProject = ProjectBuilder.builder()
            .withName("feature")
            .withParent(rootProject)
            .build()
        val featureImplProject = ProjectBuilder.builder()
            .withName("featureImpl")
            .withParent(featureProject)
            .build()
        val libraryProject = ProjectBuilder.builder()
            .withName("library")
            .withParent(featureImplProject)
            .build()
        val testingLibraryProject = ProjectBuilder.builder()
            .withName("testingLibrary")
            .withParent(libraryProject)
            .build()

        (rootProject as org.gradle.api.internal.project.DefaultProject)
            .childProjects[featureProject.name] = featureProject
        (featureProject as org.gradle.api.internal.project.DefaultProject)
            .childProjects[featureImplProject.name] = featureImplProject
        (featureImplProject as org.gradle.api.internal.project.DefaultProject)
            .childProjects[libraryProject.name] = libraryProject
        (libraryProject as org.gradle.api.internal.project.DefaultProject)
            .childProjects[testingLibraryProject.name] = testingLibraryProject

        rootProject.pluginManager.apply("java-library")
        featureProject.pluginManager.apply("java-library")
        featureImplProject.pluginManager.apply("java-library")
        libraryProject.pluginManager.apply("java-library")
        testingLibraryProject.pluginManager.apply("java-library")

        rootProject.dependencies.add("implementation", featureProject)
        featureProject.dependencies.add("implementation", featureImplProject)
        featureImplProject.dependencies.add("api", libraryProject)
        libraryProject.dependencies.add("testImplementation", testingLibraryProject)

        return rootProject
    }
}
