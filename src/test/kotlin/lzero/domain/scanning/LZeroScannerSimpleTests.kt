//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import org.junit.jupiter.api.Test

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroScannerSimpleTests
    : LZeroScannerTests() {

    @Test
    fun `Single character tokens are scanned`() {

        checkScan(
            " { } ( ) [ ] . : , = ; - ",
            LZeroToken(ELZeroTokenType.LEFT_BRACE, "{", 1, 2),
            LZeroToken(ELZeroTokenType.RIGHT_BRACE, "}", 1, 4),
            LZeroToken(ELZeroTokenType.LEFT_PARENTHESIS, "(", 1, 6),
            LZeroToken(ELZeroTokenType.RIGHT_PARENTHESIS, ")", 1, 8),
            LZeroToken(ELZeroTokenType.LEFT_BRACKET, "[", 1, 10),
            LZeroToken(ELZeroTokenType.RIGHT_BRACKET, "]", 1, 12),
            LZeroToken(ELZeroTokenType.DOT, ".", 1, 14),
            LZeroToken(ELZeroTokenType.COLON, ":", 1, 16),
            LZeroToken(ELZeroTokenType.COMMA, ",", 1, 18),
            LZeroToken(ELZeroTokenType.EQ, "=", 1, 20),
            LZeroToken(ELZeroTokenType.SEMICOLON, ";", 1, 22),
            LZeroToken(ELZeroTokenType.DASH, "-", 1, 24)
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
    fun `Quoted identifiers are scanned`() {

        checkScan(
            " `a` `this one - has punctuation; really`",
            LZeroToken(ELZeroTokenType.IDENTIFIER, "`a`", 1, 2),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "`this one - has punctuation; really`", 1, 6)
        )

    }

    @Test
    fun `Concept keywords are scanned`() {

        checkScan(
            "#abc #pqrs \n\n #xyz_123 \n",
            LZeroToken(ELZeroTokenType.CONCEPT_KEYWORD, "#abc", 1, 1),
            LZeroToken(ELZeroTokenType.CONCEPT_KEYWORD, "#pqrs", 1, 6),
            LZeroToken(ELZeroTokenType.CONCEPT_KEYWORD, "#xyz_123", 3, 2)
        )

    }

    @Test
    fun `Connector keywords are scanned`() {

        checkScan(
            "~abc ~pqrs \n\n ~xyz_123 \n",
            LZeroToken(ELZeroTokenType.CONNECTOR_KEYWORD, "~abc", 1, 1),
            LZeroToken(ELZeroTokenType.CONNECTOR_KEYWORD, "~pqrs", 1, 6),
            LZeroToken(ELZeroTokenType.CONNECTOR_KEYWORD, "~xyz_123", 3, 2)
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
    fun `UUID literals are scanned`() {

        checkScan(
            "%12345678-ABCD-EFab-cdef-901234567890\n%11111111-2222-3333-4444-555555555555",
            LZeroToken(ELZeroTokenType.UUID, "%12345678-ABCD-EFab-cdef-901234567890", 1, 1),
            LZeroToken(ELZeroTokenType.UUID, "%11111111-2222-3333-4444-555555555555", 2, 1)
        )

    }

    @Test
    fun `Documentation lines are scanned`() {

        checkScan(
            "/* this is a block of documentation */\n\n /* this is another */",
            LZeroToken(ELZeroTokenType.DOCUMENTATION, "/* this is a block of documentation */", 1, 1),
            LZeroToken(ELZeroTokenType.DOCUMENTATION, "/* this is another */", 3, 2)
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

