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
internal class CyclomaticRecursiveVisitorTest: TestTask() {

    @BeforeAll
    override fun setUp() {
        super.setUp()
    }

    @AfterAll
    override fun tearDown(){
        super.tearDown()
    }

    private fun visitCode(code: String): CyclomaticRecursiveVisitor {
        val visitor = CyclomaticRecursiveVisitor()
        ApplicationManager.getApplication().runReadAction {
            val testFile = this.createLightFile("test.py", PythonFileType.INSTANCE.language, code);
            visitor.visitFile(testFile)
        }
        return visitor
    }

    @Test
    fun `test zero CC for empty code`(){
        val code = """
            
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 0)
    }

    @Test
    fun `test simple statement`(){
        val code = """
            a = 1
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 0)
    }

    @Test
    fun `test series of simple statements`(){
        val code = """
            a = 1
            b = 2
            c = 3
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 0)
    }

    @Test
    fun `test simple if`(){
        val code = """
            if a = 1:
                pass
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 1)
    }

    @Test
    fun `test nested if`(){
        val code = """
            if a = 1:
                if b = 2:
                    pass
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 2)
    }

    @Test
    fun `test simple for loop`(){
        val code = """
            for a in b:
                pass
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 1)
    }

    @Test
    fun `test simple while loop`(){
        val code = """
            while a is True:
                pass
        """.trimIndent()
        val v = visitCode(code)
        assertEquals(v.complexity(), 1)
    }
}