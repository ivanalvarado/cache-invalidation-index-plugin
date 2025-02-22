package com.ivanalvarado.cacheinvalidationindex.writer

import com.ivanalvarado.cacheinvalidationindex.domain.model.DependencyEdge
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import org.jgrapht.graph.AbstractGraph
import org.jgrapht.nio.DefaultAttribute
import org.jgrapht.nio.dot.DOTExporter
import java.io.File
import java.io.StringWriter

class GraphVizWriter {
    fun writeGraph(
        graph: AbstractGraph<String, DependencyEdge>,
        file: File,
    ) {
        val exporter =
            DOTExporter<String, DependencyEdge> { vertex ->
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

        val writer = StringWriter()
        exporter.exportGraph(graph, writer)
        val dotString = writer.toString()

        file.delete()
        Graphviz.fromString(dotString)
            .render(Format.PNG)
            .toFile(file)
    }

    private fun String.sanitize(): String {
        return this.replace("-", "_")
            .replace(".", "_")
            .replace(":", "_")
    }
}
