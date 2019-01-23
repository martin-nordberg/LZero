//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.scanning.ELZeroTokenType
import lzero.domain.scanning.LZeroScanner
import lzero.domain.scanning.LZeroToken
import lzero.domain.scanning.StringTokenizer

//---------------------------------------------------------------------------------------------------------------------

/**
 * Implementation of LZeroExpectedTokenBuffer for testing and consuming tokens from a look-ahead buffer.
 */
internal class LZeroExpectedTokenBufferImpl(
    code: String
) : LZeroExpectedTokenBuffer,
    LZeroTokenBuffer by LZeroTokenBufferImpl(LZeroScanner(StringTokenizer(code))) {

    override fun expected(description: String): Nothing {
        val token = lookAhead(1)

        val msg = if ( token == null ) "Expected $description."
                  else "Expected $description at (${token.line},${token.column}); found '${token.text}'"

        throw IllegalArgumentException(msg)
    }

    override fun expected(vararg tokenTypes: ELZeroTokenType): Nothing {

        if (tokenTypes.size == 1) {
            expected(tokenTypes[0].text)
        }

        val tokenText = tokenTypes.joinToString(", ") { t -> t.text }
        expected("one of { $tokenText }")

    }

    override fun expected(description: String, vararg tokenTypes: ELZeroTokenType): Nothing {

        if (tokenTypes.size == 1) {
            expected("$description - ${tokenTypes[0].text}.")
        }

        val tokenText = tokenTypes.joinToString(", ") { t -> t.text }
        expected("$description - one of { $tokenText }")

    }

    override fun read(tokenType: ELZeroTokenType): LZeroToken {

        if (!hasLookAhead(tokenType)) {
            expected(tokenType)
        }

        return read()

    }

    override fun readOneOf(vararg tokenTypes: ELZeroTokenType): LZeroToken {

        for (tokenType in tokenTypes) {
            if (hasLookAhead(tokenType)) {
                return read()
            }
        }

        expected(*tokenTypes)

    }

}

//---------------------------------------------------------------------------------------------------------------------

