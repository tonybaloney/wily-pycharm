package wily.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyFile
import wily.MI
import wily.utils.loc
import wily.visitors.CyclomaticRecursiveVisitor
import wily.visitors.HalsteadRecursiveVisitor

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
    }
}

