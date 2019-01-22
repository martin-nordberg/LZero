//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package jvm.lzero.domain.scanning

import i.lzero.domain.scanning.CharBuffer
import i.lzero.domain.scanning.ELZeroTokenType
import i.lzero.domain.scanning.LZeroScanner
import i.lzero.domain.scanning.LZeroToken
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroScannerTests {

    private fun checkScan( code: String, vararg expectedTokens: LZeroToken) {
        val scanner = LZeroScanner( CharBuffer( code ) )

        for ( expectedToken in expectedTokens ) {
            val token = scanner.scan()
            assertEquals( expectedToken.type, token.type )
            assertEquals( expectedToken.text, token.text )
            assertEquals( expectedToken.line, token.line )
            assertEquals( expectedToken.column, token.column )
        }

        assertEquals( ELZeroTokenType.END_OF_INPUT, scanner.scan().type )
    }

    ////

    @Test
    fun `Single character tokens are scanned`() {

        checkScan(
            " { } ( ) . : , = ",
            LZeroToken( ELZeroTokenType.LBRACE, "{", 1,2),
            LZeroToken( ELZeroTokenType.RBRACE, "}", 1,4),
            LZeroToken( ELZeroTokenType.LPAREN, "(", 1,6 ),
            LZeroToken( ELZeroTokenType.RPAREN, ")", 1,8),
            LZeroToken( ELZeroTokenType.DOT, ".", 1,10),
            LZeroToken( ELZeroTokenType.COLON, ":", 1,12),
            LZeroToken(ELZeroTokenType.COMMA, ",", 1,14),
            LZeroToken(ELZeroTokenType.EQ, "=", 1, 16)
        )

    }

    @Test
    fun `Identifiers are scanned`() {

        checkScan(
            " a abc Ab234 _ _something rr_12_pq",
            LZeroToken(ELZeroTokenType.IDENTIFIER, "a", 1, 2),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "abc", 1, 4),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "Ab234", 1, 8),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "_", 1, 14),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "_something", 1, 16),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "rr_12_pq", 1, 27)
        )

    }

    @Test
    fun `Keywords are scanned`() {

        checkScan(
            "#abc #pqrs \n\n #xyz123 \n",
            LZeroToken(ELZeroTokenType.KEYWORD, "#abc", 1, 1),
            LZeroToken(ELZeroTokenType.KEYWORD, "#pqrs", 1, 6),
            LZeroToken(ELZeroTokenType.KEYWORD, "#xyz123", 3, 2)
        )

    }

    @Test
    fun `String literals are scanned`() {

        checkScan(
            " \"abc\" \n \"qrs\"",
            LZeroToken(ELZeroTokenType.STRING_LITERAL, "\"abc\"", 1, 2),
            LZeroToken(ELZeroTokenType.STRING_LITERAL, "\"qrs\"", 2, 2)
        )

    }

    @Test
    fun `Character literals are scanned`() {

        checkScan(
            " 'a' '\\n' '\\t' 'Q' ",
            LZeroToken(ELZeroTokenType.CHARACTER_LITERAL, "'a'", 1, 2),
            LZeroToken(ELZeroTokenType.CHARACTER_LITERAL, "'\\n'", 1, 6),
            LZeroToken(ELZeroTokenType.CHARACTER_LITERAL, "'\\t'", 1, 11),
            LZeroToken(ELZeroTokenType.CHARACTER_LITERAL, "'Q'", 1, 16)
        )

    }

    @Test
    fun `Integer literals are scanned`() {

        checkScan(
            "123\n123_456\n5678L",
            LZeroToken(ELZeroTokenType.INTEGER_LITERAL, "123", 1, 1),
            LZeroToken(ELZeroTokenType.INTEGER_LITERAL, "123_456", 2, 1),
            LZeroToken(ELZeroTokenType.INTEGER_LITERAL, "5678L", 3, 1)
        )

    }

    @Test
    fun `Floating point literals are scanned`() {

        checkScan(
            "123.0\n123_456e78f\n1.00E-30D",
            LZeroToken(ELZeroTokenType.FLOATING_POINT_LITERAL, "123.0", 1, 1),
            LZeroToken(ELZeroTokenType.FLOATING_POINT_LITERAL, "123_456e78f", 2, 1),
            LZeroToken(ELZeroTokenType.FLOATING_POINT_LITERAL, "1.00E-30D", 3, 1)
        )

    }

    @Test
    fun `Documentation lines are scanned`() {

        checkScan(
            "! this is a line of documentation\n\n ! this is another ",
            LZeroToken(ELZeroTokenType.DOCUMENTATION_LINE, "! this is a line of documentation", 1, 1),
            LZeroToken(ELZeroTokenType.DOCUMENTATION_LINE, "! this is another ", 3, 2)
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

