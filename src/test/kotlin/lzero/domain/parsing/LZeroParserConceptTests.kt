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

        assertEquals(code, element.code)
    }

    @Test
    fun `An anonymous documented concept is parsed`() {

        val code = """
            /* documentation */
            #example.sample.test
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A named documented concept is parsed`() {

        val code = """
            /* documentation */
            #example sample.test
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `An annotated concept is parsed`() {

        val code = """
            /* documentation */
            @wonderful @terrific(100)
            #example sample.test
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `An elaborate annotated concept is parsed`() {

        val code = """
            /* documentation */
            @wonderful @terrific(100, 'A', job = "stupendous")
            #example sample.test
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with UUID is parsed`() {

        val code = """
            /* documentation */
            @annotated
            #example sample.test %11111111-2222-3333-4444-555555555555%
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with an empty parameter list is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test()
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with a single parameter is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a)
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with multiple parameters is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a, b, c)
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with connected parameters is parsed`() {

        val code = """
            /* documentation */
            @annotated(value = "whatever")
            #example sample.test(a: A, b: B, c: C)
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with an implicit connection is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) : p.q.R
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with an explicit connection is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with multiple explicit connections is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z ~extends~> e.f.G
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with an explicit connection to multiple items is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> [x.y.Z, e.f.G]
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with a containment is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z { #subitem qwerty }
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with two containments is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z { #subitem qwerty; #subitem asdf }
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with multiple contained items is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z {
              #subitem qwerty; #another asdf; #athird zxcv
            }
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with no contained items is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) ~uses~> x.y.Z {}
        """.trimIndent()

        checkParseAndGenerate(code)

    }

    @Test
    fun `A concept with all kinds of connections is parsed`() {

        val code = """
            #example sample.test(a: A, b: B, c: C) : p.q.R ~uses~> x.y.Z ~extends~> e.f.G {
              #subitem qwerty; #another asdf = "5"; #aUuid %11111111-2222-3333-4444-555555555555%
            }
        """.trimIndent()

        checkParseAndGenerate(code)

    }

}

//---------------------------------------------------------------------------------------------------------------------

