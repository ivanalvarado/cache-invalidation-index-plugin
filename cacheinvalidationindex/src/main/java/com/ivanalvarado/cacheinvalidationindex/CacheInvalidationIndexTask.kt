package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.TaskAction

open class CacheInvalidationIndexTask : DefaultTask() {

    @TaskAction
    fun run() {
        val dependencyPairs = project.rootProject.dependencyPairs()
        println("Dependency Pairs: $dependencyPairs")
    }

    private fun Project.dependencyPairs(): List<Triple<Project, Project, String>> {
        return subprojects.flatMap { project ->
            project.configurations
                .filter { it.name == "implementation" }
                .flatMap { configuration ->
                    println("Configuration: ${configuration.name}")
                    configuration.dependencies.filterIsInstance<ProjectDependency>()
                        .map { Triple(project, it.dependencyProject, configuration.name) }
                }
        }
    }
}
