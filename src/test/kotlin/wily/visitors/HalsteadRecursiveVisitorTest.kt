package wily.visitors

import com.intellij.openapi.application.ApplicationManager
import com.jetbrains.python.PythonFileType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import wily.TestTask

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HalsteadRecursiveVisitorTest: TestTask() {

    @BeforeAll
    override fun setUp() {
        super.setUp()
    }

    @AfterAll
    override fun tearDown(){
        super.tearDown()
    }

    private fun visitCode(code: String): HalsteadRecursiveVisitor {
        val visitor = HalsteadRecursiveVisitor()
        ApplicationManager.getApplication().runReadAction {
            val testFile = this.createLightFile("test.py", PythonFileType.INSTANCE.language, code);
            visitor.visitFile(testFile)
        }
        return visitor
    }

    @Test
    fun `test simple statement`(){
        val code = """
            a = 1
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.vocabulary(), 3)
        assertEquals(v.length(), 3)
    }
}