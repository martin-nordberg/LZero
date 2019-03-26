//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroParserConceptTests {

    private fun checkParseAndGenerate(code: String) {
        val parser = LZeroParser("test.lzero", code)
        val element = parser.parseElement()

        assertEquals(code, element.text)
    }

    @Test
    fun `An anonymous documented concept is parsed`() {

        val code = """
            /* documentation */
            #example.sample.test;
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A named documented concept is parsed`() {

        val code = """
            /* documentation */
            #example sample.test;
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `An annotated concept is parsed`() {

        val code = """
            /* documentation */
            @wonderful @terrific(100)
            #example sample.test;
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `An elaborate annotated concept is parsed`() {

        val code = """
            /* documentation */
            @wonderful @terrific(100, 'A', job = "stupendous")
            #example sample.test;
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with UUID is parsed`() {

        val code = """
            /* documentation */
            @annotated
            #example sample.test %11111111-2222-3333-4444-555555555555%;
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with an empty parameter list is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test();
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with a single parameter is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a);
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with multiple parameters is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a, b, c);
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with connected parameters is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a: A, b: B, c: C);
        """.trimIndent()

        checkParseAndGenerate(code)

    }

}

//---------------------------------------------------------------------------------------------------------------------

