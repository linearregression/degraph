package de.schauderhaft.degraph.configuration

import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import de.schauderhaft.degraph.graph.SliceSource
import scalax.collection.edge.LkDiEdge
import scalax.collection.mutable.{ Graph => SGraph }
import de.schauderhaft.degraph.model.Node
import de.schauderhaft.degraph.graph.Graph
import de.schauderhaft.degraph.model.SimpleNode

@RunWith(classOf[JUnitRunner])
class LayeringConstraintTest extends FunSuite with ShouldMatchers {
    val c = LayeringConstraint("t", IndexedSeq(Layer("a"), Layer("b"), Layer("c")))

    test("simple violation free graph returns empty Set of violations") {
        c.violations(MockSliceSource("t", "a" -> "b", "b" -> "c")) should be(Set())
    }

    test("it's ok to skip layers") {
        c.violations(MockSliceSource("t", "a" -> "c")) should be(Set())
    }

    test("reverse dependency is reported as a violation") {
        c.violations(MockSliceSource("t", "b" -> "a")) should be(Set((SimpleNode("t", "b"), SimpleNode("t", "a"))))
    }
    test("dependencies in other layers are ignored") {
        c.violations(MockSliceSource("x", "b" -> "a")) should be(Set())
    }

    test("dependency to unknown is ok when from last") {
        c.violations(MockSliceSource("t", "c" -> "x")) should be(Set())
    }

    test("dependency from unknown is ok when to first") {
        c.violations(MockSliceSource("t", "x" -> "a")) should be(Set())
    }
    test("dependency to unknown is ok when from middle") {
        c.violations(MockSliceSource("t", "b" -> "x")) should be(Set())
    }

    test("dependency from unknown is ok when to middle") {
        c.violations(MockSliceSource("t", "x" -> "b")) should be(Set())
    }

}