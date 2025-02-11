package com.ivanalvarado.cacheinvalidationindex.domain.usecase

import org.gradle.api.Project

class FindDependencyPairsImpl : FindDependencyPairs  {
    override fun invoke(
        project: Project,
        configurationsToAnalyse: Set<String>
    ): List<Triple<Project, Project, String>> {
        return emptyList()
    }
}
