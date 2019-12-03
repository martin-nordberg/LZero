//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.builders

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class CodeStringBuilderTests {

    @Test
    fun `Simple output is concatenated as expected`() {

        val builder = CodeStringBuilder()

        builder.append("some code")
        builder.append(" with not much to it")

        assertEquals("some code with not much to it", builder.toString())

    }

    @Test
    fun `New lines start as expected`() {

        val builder = CodeStringBuilder()

        builder.append("some code\n")
        builder.append("with not")
        builder.appendNewLine()
        builder.append("much to it")

        assertEquals("some code\nwith not\nmuch to it", builder.toString())

    }

    @Test
    fun `Indentation occurs as expected`() {

        val builder = CodeStringBuilder()

        builder.append("some code {\n")
        builder.indented {
            this.append("indented\nalso indented\nand more\n")
        }
        builder.append("}")

        assertEquals("some code {\n  indented\n  also indented\n  and more\n}", builder.toString())

    }

    @Test
    fun `Indentation white space is configurable`() {

        val builder = CodeStringBuilder("    ")

        builder.append("some code {\n")
        builder.indented {
            this.append("indented\nalso indented\nand more\n")
        }
        builder.append("}")

        assertEquals("some code {\n    indented\n    also indented\n    and more\n}", builder.toString())

    }

    @Test
    fun `Indentation mid-line has no effect`() {

        val builder = CodeStringBuilder()

        builder.append("some code {")
        builder.indented {
            this.append(" not indented; also not indented\n")
        }
        builder.append("}")

        assertEquals("some code { not indented; also not indented\n}", builder.toString())

    }

    @Test
    fun `Trailing spaces are removed`() {

        val builder = CodeStringBuilder()

        builder.append("some code { \n  ")
        builder.indented {
            this.append("indented  \n ")
        }
        builder.append("} ")

        assertEquals("some code {\n  indented\n}", builder.toString())

    }

    @Test
    fun `Indentation via nesting occurs as expected`() {

        val builder = CodeStringBuilder("\t")

        builder.append("some code {\n")
        builder.indented {
            this.append("indented\nalso indented\n")
            builder.indented {
                this.append("double indented\n")
            }
        }
        builder.append("}")

        assertEquals("some code {\n\tindented\n\talso indented\n\t\tdouble indented\n}", builder.toString())

    }

    @Test
    fun `A trial append works for short line length`() {

        val builder = CodeStringBuilder("\t", 10)

        val written = builder.lineLengthLimited {
            append("some code")
        }

        assertTrue( written)
        assertEquals("some code", builder.toString())

    }

    @Test
    fun `A trial append fails for short line length`() {

        val builder = CodeStringBuilder("\t", 10)

        builder.append("some code")

        val written = builder.lineLengthLimited {
            append(" that is too long")
        }

        assertFalse(written)
        assertEquals("some code", builder.toString())

    }

    @Test
    fun `A trial append fails for optional new line`() {

        val builder = CodeStringBuilder("\t", 10)

        builder.append("some")

        val written = builder.lineLengthLimited {
            append( "code" + CodeStringBuilder.SOFT_HYPHEN + "divided")
        }

        assertFalse(written)
        assertEquals("some", builder.toString())

    }

}

//---------------------------------------------------------------------------------------------------------------------

