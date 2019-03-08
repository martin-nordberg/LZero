//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroParserDocumentationTests {

    @Test
    fun `An element's documentation is parsed`() {

        val code = """
            /* first line of documentation
               second line of documentation */
            #example;
        """.trimIndent()

        val parser = LZeroParser("test.lzero", code)

        val element = parser.parseElement()

        assertNotNull( element.documentation )
        assertEquals( "/* first line of documentation\n" +
                "   second line of documentation */", element.documentation.text )
        assertEquals( "test.lzero(1,1)", element.documentation.origin.toString() )

        assertEquals( "#example", element.concept.text )
        assertEquals( "test.lzero(3,1)", element.origin.toString() )
        assertEquals( "test.lzero(3,1)", element.concept.origin.toString() )

    }

}

//---------------------------------------------------------------------------------------------------------------------

