package de.schauderhaft.degraph.analysis

import java.io.File
import scala.io.Source
import de.schauderhaft.degraph.graph.Graph
import de.schauderhaft.degraph.graph.Graph
import scala.xml.pull.XMLEventReader
import scala.xml.pull.EvElemStart
import scala.xml.MetaData

/**
 * provides a single method that
 * - takes a list of source paths
 * - searches in these paths for spring xml files
 * and creates a Graph object with the following properties:
 * - Every spring file is a node
 * - every import of a file means that file depends on the imported file
 * - Every bean is a node
 * - Every bean is contained in the category represented by the file it is defined in
 * - Every bean depends on the class it is implemented by
 * - if A gets injected into B  B depends on A
 */

object SpringAnalyzer {

    def analyze(file: File): Graph = {
        val g = new Graph
        if (file != null) {

            val srcFile = Source.fromFile(file)
            val reader = new XMLEventReader(srcFile)

            for {
                event <- reader
                if (event.isInstanceOf[EvElemStart])
                EvElemStart(_, name, attributes, _) = event
                if (name == "bean")
            } g.add(makeGraphElement(attributes))
        }

        g
    }

    private def makeGraphElement(attributes: MetaData) = attributes.get("class").get(0).toString

}