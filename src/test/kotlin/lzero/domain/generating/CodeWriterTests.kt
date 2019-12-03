//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class CodeWriterTests {

    @Test
    fun `Simple output is concatenated as expected`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.write(" with not much to it")

        assertEquals("some code with not much to it", writer.toString())

    }

    @Test
    fun `New lines start as expected`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeNewLine()
        writer.write("with not")
        writer.writeNewLine()
        writer.write("much to it")

        assertEquals("some code\nwith not\nmuch to it", writer.toString())

    }

    @Test
    fun `A brace block can fit on one line`() {

        val writer = CodeWriter()

        writer.write("some code ")
        writer.writeBraceSemicolonBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code { item1; item2; item3 }", writer.toString())

    }

    @Test
    fun `A brace block can take an extra line`() {

        val writer = CodeWriter()

        writer.write("some code ")
        writer.writeBraceSemicolonBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code {\n  item1; item2; item3\n}", writer.toCodeString("  ",21))

    }

    @Test
    fun `A brace block can take one line per item`() {

        val writer = CodeWriter()

        writer.write("some code ")
        writer.writeBraceSemicolonBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code {\n  item1\n  item2\n  item3\n}", writer.toCodeString("  ",15))

    }

    @Test
    fun `A paren block can fit on one line`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeParenCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code(item1, item2, item3)", writer.toString())

    }

    @Test
    fun `A paren block accommodates complex items`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeParenCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item, ": ", "Type")
        }

        assertEquals("some code(item1: Type, item2: Type, item3: Type)", writer.toString())

    }

    @Test
    fun `A paren block can take an extra line`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeParenCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code(\n  item1, item2, item3\n)", writer.toCodeString("  ",21))

    }

    @Test
    fun `A paren block can take one line per item`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeParenCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code(\n  item1,\n  item2,\n  item3\n)", writer.toCodeString("  ",15))

    }

    @Test
    fun `A square bracket block can fit on one line`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeSqBracketCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code[item1, item2, item3]", writer.toString())

    }

    @Test
    fun `A square bracket block can take an extra line`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeSqBracketCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code[\n  item1, item2, item3\n]", writer.toCodeString("  ",21))

    }

    @Test
    fun `A square bracket block can take one line per item`() {

        val writer = CodeWriter()

        writer.write("some code")
        writer.writeSqBracketCommaBlock(listOf("item1", "item2", "item3") ) { item ->
            this.write(item)
        }

        assertEquals("some code[\n  item1,\n  item2,\n  item3\n]", writer.toCodeString("  ",15))

    }

}

//---------------------------------------------------------------------------------------------------------------------

