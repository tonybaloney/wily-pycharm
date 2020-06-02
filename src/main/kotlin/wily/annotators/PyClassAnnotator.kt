package wily.annotators

import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.validation.PyAnnotator
import wily.MI
import wily.asHighlightingSeverity
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
        val range = node.nameNode?.textRange ?: node.textRange
        this.holder.createAnnotation(asHighlightingSeverity(mi), range, "${node.name} Maintainability: $mi %")
    }
}