package com.ivanalvarado.cacheinvalidationindex.writer

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import org.jgrapht.graph.AbstractGraph
import org.jgrapht.nio.DefaultAttribute
import org.jgrapht.nio.dot.DOTExporter
import java.io.File

class GraphVizWriter {

    fun writeGraph(graph: AbstractGraph<String, DependencyEdge>, file: File) {
        val exporter = DOTExporter<String, DependencyEdge> { vertex ->
            vertex.sanitize()
        }

        exporter.setVertexAttributeProvider { vertex ->
            buildMap {
                put("label", DefaultAttribute.createAttribute(vertex))
            }
        }

        exporter.setEdgeAttributeProvider { edge ->
            buildMap {
                put("label", DefaultAttribute.createAttribute(edge.configuration))
            }
        }

        file.delete()
        exporter.exportGraph(graph, file)
    }

    private fun String.sanitize(): String {
        return this.replace("-", "_")
            .replace(".", "_")
            .replace(":", "_")
    }
}
