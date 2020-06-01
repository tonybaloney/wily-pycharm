package wily.annotators

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.validation.PyAnnotator
import wily.MI
import wily.utils.loc
import wily.visitors.CyclomaticRecursiveVisitor
import wily.visitors.HalsteadRecursiveVisitor
import kotlin.math.roundToInt

class PyFunctionAnnotator : PyAnnotator(){
    override fun visitPyFunction(node: PyFunction) {
        val halVisitor = HalsteadRecursiveVisitor()
        val cyclomaticVisitor = CyclomaticRecursiveVisitor()
        halVisitor.visitElement(node)
        cyclomaticVisitor.visitElement(node)
        val mi = MI(halVisitor.volume(), cyclomaticVisitor.complexity(), node.loc()).roundToInt()
        this.holder.createAnnotation(HighlightSeverity.INFORMATION, node.nameNode!!.textRange, "${node.name} Maintainability: $mi %")
    }
}