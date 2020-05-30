package wily.visitors

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import com.jetbrains.python.psi.PyElement
import kotlin.math.log2

class HalsteadRecursiveVisitor() : PsiRecursiveElementVisitor() {
    private var operators: HashSet<IElementType?> = hashSetOf()
    private var operands: HashSet<String> = hashSetOf()
    private var operandsCount: Int = 0
    private var operatorsCount: Int = 0

    override fun visitElement(element: PsiElement?) {
        if (!operators.contains(element.elementType))
            this.operators.add(element.elementType)
        operatorsCount++
        if (element is PyElement) {
            operandsCount++
            if (!operands.contains(element.name))
                element.name?.let { operands.add(it) }
        }

        super.visitElement(element)
    }

    fun vocabulary(): Int {
        return this.operators.size + this.operands.size
    }

    fun length(): Int {
        return this.operandsCount + this.operatorsCount
    }

    fun volume(): Double {
        return this.length() * log2(this.vocabulary().toDouble())
    }

    fun difficulty(): Int {
        return (this.operators.size/2) * (this.operandsCount/this.operands.size)
    }

    fun effort(): Double {
        return this.difficulty() * this.volume()
    }
}