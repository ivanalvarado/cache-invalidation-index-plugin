package com.ivanalvarado.cacheinvalidationindex.writer

import com.ivanalvarado.cacheinvalidationindex.domain.model.CacheInvalidationResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class CacheInvalidationIndexWriter {
    fun writeIndex(
        project: String,
        index: Int,
        file: File,
    ) {
        val result =
            CacheInvalidationResult(
                projectName = project,
                cacheInvalidationIndex = index,
            )

        val moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val jsonAdapter = moshi.adapter(CacheInvalidationResult::class.java)
        val jsonOutput = jsonAdapter.toJson(result)

        file.writeText(jsonOutput)
    }
}
