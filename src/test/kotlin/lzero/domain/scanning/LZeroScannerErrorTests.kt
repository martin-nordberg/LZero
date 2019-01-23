//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import lzero.domain.scanning.ELZeroTokenType
import lzero.domain.scanning.LZeroToken
import org.junit.jupiter.api.Test

//---------------------------------------------------------------------------------------------------------------------

@Suppress("RemoveRedundantBackticks")
class LZeroScannerErrorTests
    : LZeroScannerTests() {

    @Test
    fun `Invalid characters are scanned`() {

        checkScan(
            " #key id %% ! doc ",
            LZeroToken( ELZeroTokenType.KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.IDENTIFIER, "id", 1,7),
            LZeroToken( ELZeroTokenType.INVALID_CHARACTER, "%", 1,10 ),
            LZeroToken( ELZeroTokenType.INVALID_CHARACTER, "%", 1,11 ),
            LZeroToken( ELZeroTokenType.DOCUMENTATION_LINE, "! doc ", 1, 13)
        )

    }

    @Test
    fun `Unterminated string literals are scanned`() {

        checkScan(
            " #key \"first \n\"second ",
            LZeroToken( ELZeroTokenType.KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"first ", 1,7),
            LZeroToken( ELZeroTokenType.UNTERMINATED_STRING_LITERAL, "\"second ", 2, 1)
        )

    }

    @Test
    fun `Empty keywords are scanned`() {

        checkScan(
            "#key #\n# #ok",
            LZeroToken( ELZeroTokenType.KEYWORD, "#key", 1,1),
            LZeroToken( ELZeroTokenType.EMPTY_KEYWORD, "#", 1, 6),
            LZeroToken( ELZeroTokenType.EMPTY_KEYWORD, "#", 2, 1),
            LZeroToken( ELZeroTokenType.KEYWORD, "#ok", 2, 3)
        )

    }

    @Test
    fun `Unterminated character literals are scanned`() {

        checkScan(
            " #key '1\n'2",
            LZeroToken( ELZeroTokenType.KEYWORD, "#key", 1,2),
            LZeroToken( ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'1", 1,7),
            LZeroToken( ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL, "'2", 2, 1)
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

