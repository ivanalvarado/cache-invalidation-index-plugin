package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class FindDependencyPairsImpl : FindDependencyPairs {
    override fun invoke(
        project: Project,
        configurationsToAnalyse: Set<String>,
    ): List<Triple<Project, Project, String>> {
        return project.subprojects.flatMap { subproject ->
            subproject.configurations
                .filter { configurationsToAnalyse.contains(it.name) }
                .flatMap { configuration ->
                    configuration.dependencies.filterIsInstance<ProjectDependency>()
                        .map { Triple(subproject, it.dependencyProject, configuration.name) }
                }
        }
    }
}
