package de.schauderhaft.dependencies
import de.schauderhaft.dependencies.analysis.Analyzer
import de.schauderhaft.dependencies.writer.Writer
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import writer.WriterTest
import scala.xml.XML
import de.schauderhaft.dependencies.categorizer.InternalClassCategorizer
import de.schauderhaft.dependencies.categorizer.PackageCategorizer
import de.schauderhaft.dependencies.categorizer.MultiCategorizer.combine
import de.schauderhaft.dependencies.filter.NoJdk

/**
 * the main class of the DependencyManager, plugging everything together, starting the analysis process and writing the result to an XML file
 */
object DependencyManager {

    def main(args : Array[String]) : Unit = {
        val g = Analyzer.analyze("./lib/junit-4.8.2.jar",
            combine(InternalClassCategorizer, PackageCategorizer),
            NoJdk)

        val xml = (new Writer()).toXml(g)
        XML.save("exampleX.graphml", xml, "UTF8", true, null)
    }

}