package com.ivanalvarado.cacheinvalidationindex

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CacheInvalidationIndexTask : DefaultTask() {

    @TaskAction
    fun run() {
        println("Hello from the CacheInvalidationIndexTask!")
    }
}
