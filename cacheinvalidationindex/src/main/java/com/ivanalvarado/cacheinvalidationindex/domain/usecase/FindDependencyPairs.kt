package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import org.gradle.api.Project

interface FindDependencyPairs {
    operator fun invoke(
        project: Project,
        configurationsToAnalyse: Set<String>
    ): List<Triple<Project, Project, String>>
}
