package wily.annotators

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.validation.PyAnnotator
import wily.MI
import wily.utils.loc
import wily.visitors.CyclomaticRecursiveVisitor
import wily.visitors.HalsteadRecursiveVisitor
import kotlin.math.roundToInt

class PyClassAnnotator : PyAnnotator(){
    override fun visitPyClass(node: PyClass) {
        val halVisitor = HalsteadRecursiveVisitor()
        val cyclomaticVisitor = CyclomaticRecursiveVisitor()
        halVisitor.visitElement(node)
        cyclomaticVisitor.visitElement(node)
        val mi = MI(halVisitor.volume(), cyclomaticVisitor.complexity(), node.loc()).roundToInt()
        this.holder.createAnnotation(HighlightSeverity.INFORMATION, node.nameNode!!.textRange, "${node.qualifiedName} Maintainability: $mi %, Cyclomatic Complexity: ${cyclomaticVisitor.complexity()}")
    }
}