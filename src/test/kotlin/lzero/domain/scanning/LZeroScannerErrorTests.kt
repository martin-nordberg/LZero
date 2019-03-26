//
// (C) Copyright 2018-2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import org.junit.jupiter.api.Test

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroScannerErrorTests
    : LZeroScannerTests() {

    @Test
    fun `Invalid characters are scanned`() {

        checkScan(
            " #key id ^^ /* doc */ ",
            LZeroToken(ELZeroTokenType.HASH, "#", 1, 2),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "key", 1, 3),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "id", 1, 7),
            LZeroToken(ELZeroTokenType.INVALID_CHARACTER, "^", 1, 10),
            LZeroToken(ELZeroTokenType.INVALID_CHARACTER, "^", 1, 11),
            LZeroToken(ELZeroTokenType.DOCUMENTATION, "/* doc */", 1, 13)
        )

    }

    @Test
    fun `Unterminated string literals are scanned`() {

        checkScan(
            " #key \"first \n\"second ",
            LZeroToken(ELZeroTokenType.HASH, "#", 1, 2),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "key", 1, 3),
            LZeroToken(ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"first ", 1, 7),
            LZeroToken(ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"second ", 2, 1)
        )

    }

    @Test
    fun `Empty concept keywords are scanned`() {

        checkScan(
            "#key #2\n#- #ok",
            LZeroToken(ELZeroTokenType.HASH, "#", 1, 1),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "key", 1, 2),
            LZeroToken(ELZeroTokenType.HASH, "#", 1, 6),
            LZeroToken(ELZeroTokenType.INTEGER_LITERAL, "2", 1, 7),
            LZeroToken(ELZeroTokenType.HASH, "#", 2, 1),
            LZeroToken(ELZeroTokenType.DASH, "-", 2, 2),
            LZeroToken(ELZeroTokenType.HASH, "#", 2, 4),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "ok", 2, 5)
        )

    }

    @Test
    fun `Unterminated character literals are scanned`() {

        checkScan(
            " #key '1\n'2",
            LZeroToken(ELZeroTokenType.HASH, "#", 1, 2),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "key", 1, 3),
            LZeroToken(ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'1", 1, 7),
            LZeroToken(ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'2", 2, 1)
        )

    }

    @Test
    fun `Unterminated documentation is scanned`() {

        checkScan(
            "/* starts but does not end\n",
            LZeroToken(ELZeroTokenType.UNTERMINATED_DOCUMENTATION, "/* starts but does not end\n", 1, 1)
        )

    }

    @Test
    fun `Invalid UUID literals are scanned`() {

        checkScan(
            " %1234-1234-1234 %abcW %123456789--%",
            LZeroToken(ELZeroTokenType.INVALID_UUID_LITERAL, "%1234-1234-1234", 1, 2),
            LZeroToken(ELZeroTokenType.INVALID_UUID_LITERAL, "%abc", 1, 18),
            LZeroToken(ELZeroTokenType.IDENTIFIER, "W", 1, 22),
            LZeroToken(ELZeroTokenType.INVALID_UUID_LITERAL, "%123456789--%", 1, 24)
        )

    }

    @Test
    fun `Unterminated quoted identifiers are scanned`() {

        checkScan(
            " `the first` `the second\n`the third",
            LZeroToken(ELZeroTokenType.IDENTIFIER, "`the first`", 1, 2),
            LZeroToken(ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER, "`the second", 1, 14),
            LZeroToken(ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER, "`the third", 2, 1)
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

