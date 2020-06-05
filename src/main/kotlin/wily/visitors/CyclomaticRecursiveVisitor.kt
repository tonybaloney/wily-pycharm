package wily.visitors

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.jetbrains.python.psi.PyConditionalExpression
import com.jetbrains.python.psi.PyStatementWithElse

class CyclomaticRecursiveVisitor : PsiRecursiveElementVisitor() {
    private var nodeCount : Int = 0
    private var branchCount : Int = 0

    override fun visitElement(element: PsiElement) {
        nodeCount++

        if (element is PyStatementWithElse ||
            element is PyConditionalExpression)
            branchCount++
        super.visitElement(element)
    }

    fun complexity(): Int {
        return this.branchCount
    }
}