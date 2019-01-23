//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import kotlin.test.assertEquals

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
abstract class LZeroScannerTests {

    protected fun checkScan( code: String, vararg expectedTokens: LZeroToken) {

        val scanner = LZeroScanner( StringTokenizer( code ) )

        for ( expectedToken in expectedTokens ) {
            val token = scanner.scan()
            assertEquals( expectedToken.type, token.type )
            assertEquals( expectedToken.text, token.text )
            assertEquals( expectedToken.line, token.line )
            assertEquals( expectedToken.column, token.column )
            assertEquals( expectedToken.text.length, token.length )
        }

        assertEquals( ELZeroTokenType.END_OF_INPUT, scanner.scan().type )

    }

}

//---------------------------------------------------------------------------------------------------------------------

