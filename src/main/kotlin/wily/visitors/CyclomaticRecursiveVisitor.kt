package wily.visitors

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.util.elementType
import com.jetbrains.python.psi.PyForStatement
import com.jetbrains.python.psi.PyIfStatement
import kotlin.reflect.typeOf

class CyclomaticRecursiveVisitor() : PsiRecursiveElementVisitor() {
    var nodeCount : Int = 0
    var branchCount : Int = 0

    val branchTypes = arrayOf(
        PyIfStatement::class.java,
        PyForStatement::class.java) // TODO etc.

    override fun visitElement(element: PsiElement) {
        nodeCount++
        if (branchTypes.contains(element::class.java))
            branchCount++
        super.visitElement(element)
    }

    fun complexity(): Int {
        return this.branchCount
    }
}