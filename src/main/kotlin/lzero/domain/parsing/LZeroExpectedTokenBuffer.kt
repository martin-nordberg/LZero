//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.scanning.ELZeroTokenType
import lzero.domain.scanning.LZeroToken

//---------------------------------------------------------------------------------------------------------------------

/**
 * Interface adding the ability to read or trigger errors for expected tokens.
 */
internal interface LZeroExpectedTokenBuffer
    : LZeroTokenBuffer {

    /**
     * Throws ane error indicating the expected [description] was not found in the input.
     */
    fun expected(description: String): Nothing

    /**
     * Throws an error indicating that any one of the given [tokenTypes] was expected.
     */
    fun expected(vararg tokenTypes: ELZeroTokenType): Nothing

    /**
     * Throws an error indicating that any one of the given [tokenTypes] was expected preceded by a [description]
     * of those token types.
     */
    fun expected(description: String, vararg tokenTypes: ELZeroTokenType): Nothing

    /**
     * Determines whether the look ahead token occurs on a new line from the last read token.
     */
    fun hasLookAheadOnNewLine(): Boolean

    /**
     * Reads one token expected to be of the given [tokenType].
     */
    fun read(tokenType: ELZeroTokenType): LZeroToken

    /**
     * Reads one token expected to have one of the given [tokenTypes].
     */
    fun readOneOf(vararg tokenTypes: ELZeroTokenType): LZeroToken

}

//---------------------------------------------------------------------------------------------------------------------

