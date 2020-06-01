package wily.utils

import com.intellij.psi.PsiElement

fun PsiElement.loc(): Int {
    return this.text.count { it == '\n' }
}