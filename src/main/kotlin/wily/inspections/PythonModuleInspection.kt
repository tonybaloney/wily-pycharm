package wily.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyFile
import wily.visitors.CyclomaticRecursiveVisitor
import wily.visitors.HalsteadRecursiveVisitor
import kotlin.math.ln

class PythonModuleInspection : PyInspection() {
    override fun getStaticDescription(): String? {
        return "Python Module Inspection"
    }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    private class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {
        override fun visitPyFile(node: PyFile)
        {
            val halVisitor = HalsteadRecursiveVisitor()
            val cyclomaticVisitor = CyclomaticRecursiveVisitor()
            halVisitor.visitFile(node)
            cyclomaticVisitor.visitFile(node)
            val mi = MI(halVisitor.volume(), cyclomaticVisitor.complexity(), node.loc())
            if (mi < 10)
                this.holder?.registerProblem(node, "Code is unmaintainable!")
        }

        fun MI (V: Double, G: Int, LOC: Int): Double {
            return 171 - 5.2 * ln(V) - 0.23 * (G) - 16.2 * ln(LOC.toDouble())
        }
    }

}

private fun PsiElement.loc(): Int {
    return this.text.count { it == '\n' }
}
