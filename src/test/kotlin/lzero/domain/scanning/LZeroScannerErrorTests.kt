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
            LZeroToken( ELZeroTokenType.CONCEPT_KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.IDENTIFIER, "id", 1,7),
            LZeroToken( ELZeroTokenType.INVALID_CHARACTER, "^", 1,10 ),
            LZeroToken( ELZeroTokenType.INVALID_CHARACTER, "^", 1,11 ),
            LZeroToken( ELZeroTokenType.DOCUMENTATION, "/* doc */", 1, 13)
        )

    }

    @Test
    fun `Unterminated string literals are scanned`() {

        checkScan(
            " #key \"first \n\"second ",
            LZeroToken( ELZeroTokenType.CONCEPT_KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"first ", 1,7),
            LZeroToken( ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"second ", 2, 1)
        )

    }

    @Test
    fun `Empty concept keywords are scanned`() {

        checkScan(
            "#key #2\n#- #ok",
            LZeroToken( ELZeroTokenType.CONCEPT_KEYWORD, "#key", 1,1),
            LZeroToken( ELZeroTokenType.HASH, "#", 1, 6),
            LZeroToken( ELZeroTokenType.INTEGER_LITERAL, "2", 1, 7),
            LZeroToken( ELZeroTokenType.HASH, "#", 2, 1),
            LZeroToken( ELZeroTokenType.DASH, "-", 2, 2),
            LZeroToken( ELZeroTokenType.CONCEPT_KEYWORD, "#ok", 2, 4)
        )

    }

    @Test
    fun `Unterminated character literals are scanned`() {

        checkScan(
            " #key '1\n'2",
            LZeroToken( ELZeroTokenType.CONCEPT_KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'1", 1,7),
            LZeroToken( ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'2", 2, 1)
        )

    }

    @Test
    fun `Invalid UUID literals are scanned`() {

        checkScan(
            " %1234-1234-1234 %abcW",
            LZeroToken( ELZeroTokenType.INVALID_UUID_LITERAL, "%1234-1234-1234", 1, 2),
            LZeroToken( ELZeroTokenType.INVALID_UUID_LITERAL, "%abc", 1, 18),
            LZeroToken( ELZeroTokenType.IDENTIFIER, "W", 1, 22)
        )

    }

    @Test
    fun `Unterminated quoted identifiers are scanned`() {

        checkScan(
            " `the first` `the second\n`the third",
            LZeroToken( ELZeroTokenType.IDENTIFIER, "`the first`", 1,2),
            LZeroToken( ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER, "`the second", 1,14),
            LZeroToken( ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER, "`the third", 2, 1)
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

