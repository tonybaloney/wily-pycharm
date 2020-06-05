package wily

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.python.PythonFileType
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.resolve.PyResolveContext
import com.jetbrains.python.psi.types.TypeEvalContext
import com.nhaarman.mockitokotlin2.*
import org.jetbrains.annotations.NotNull
import org.mockito.ArgumentMatchers.contains
import org.mockito.Mockito


open class TestTask: BasePlatformTestCase() {
    fun <inspector: PyInspection>testCodeAssignmentStatement(code: String, times: Int = 1, check: Checks.CheckType, filename: String = "test.py", instance: inspector){
        ApplicationManager.getApplication().runReadAction {
            val mockHolder = mock<ProblemsHolder> {
                on { registerProblem(any<PsiElement>(), contains(check.Code), anyVararg<LocalQuickFix>()) } doAnswer {}
                on { registerProblem(any<PsiElement>(), contains(check.Code), any<ProblemHighlightType>(), anyVararg<LocalQuickFix>()) } doAnswer {}
            }
            val testFile = this.createLightFile(filename, PythonFileType.INSTANCE.language, code);
            val mockLocalSession = mock<LocalInspectionToolSession> {
                on { file } doReturn (testFile)
            }
            assertNotNull(testFile)
            val testVisitor = instance.buildVisitor(mockHolder, true, mockLocalSession) as PyInspectionVisitor

            val expr: @NotNull MutableCollection<PyAssignmentStatement> = PsiTreeUtil.findChildrenOfType(testFile, PyAssignmentStatement::class.java)
            assertNotNull(expr)
            expr.forEach { e ->
                testVisitor.visitPyAssignmentStatement(e)
            }
            try {
                Mockito.verify(mockHolder, Mockito.times(times)).registerProblem(any<PsiElement>(), contains(check.Code), anyVararg<LocalQuickFix>())
            } catch (a: AssertionError){
                Mockito.verify(mockHolder, Mockito.times(times)).registerProblem(any<PsiElement>(), contains(check.Code), any<ProblemHighlightType>(), anyVararg<LocalQuickFix>())
            }
            Mockito.verify(mockLocalSession, Mockito.times(1)).file
        }
    }
}