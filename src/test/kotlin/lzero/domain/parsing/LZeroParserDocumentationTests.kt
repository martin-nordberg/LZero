//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroParserDocumentationTests {

    @Test
    fun `An element's documentation is parsed`() {

        val code = """
            ! first line of documentation
            ! second line of documentation
            #example
        """.trimIndent()

        val parser = LZeroParser("test.lzero", code)

        val element = parser.parseElement()

        assertEquals( 2, element.documentation.lines.size )
        assertEquals( "! first line of documentation", element.documentation.lines[0].text )
        assertEquals( "test.lzero(1,1)", element.documentation.origin.toString() )
        assertEquals( "test.lzero(1,1)", element.documentation.lines[0].origin.toString() )
        assertEquals( "! second line of documentation", element.documentation.lines[1].text )
        assertEquals( "test.lzero(2,1)", element.documentation.lines[1].origin.toString() )

        assertEquals( "#example", element.keyword.text )
        assertEquals( "test.lzero(3,1)", element.origin.toString() )
        assertEquals( "test.lzero(3,1)", element.keyword.origin.toString() )

    }

}

//---------------------------------------------------------------------------------------------------------------------

