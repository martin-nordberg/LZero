//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

//---------------------------------------------------------------------------------------------------------------------

/**
 * Class serving to "tokenize" the characters of a string. Serves to mark the beginnings and ends of tokens
 * under the direction of a scanner. Counts lines and columns by character position. Lines and columns are 1-based.
 * Produces L-Zero tokens from a marked starting point and a look-ahead ending point. Provides one character of
 * look-ahead to the scanner.
 */
class StringTokenizer(
    private val input: String
) {

    /** The column number of the next not-yet-consumed character. */
    private var myLookAheadColumn = 1

    /** The index (within the whole string) of the next not-yet-consumed character. */
    private var myLookAheadIndex = 0

    /** The line number of the next not-yet-consumed character. */
    private var myLookAheadLine: Int = 1

    /** The column number of the marked start of a token while it is under construction by the scanner. */
    private var myMarkedColumn = 1

    /** The index (within the whole string) of the marked start of a token while it is under construction by the scanner. */
    private var myMarkedIndex = 0

    /** The line number of the marked start of a token while it is under construction by the scanner. */
    private var myMarkedLine: Int = 1

    ////

    /**
     * Advances the position of this tokenizer. Moves the look ahead index forward one character (unless it is
     * already at the end of the input). Tracks the line number and column number. (Line breaks are denoted by '\n').
     */
    fun advance() {

        if ( myLookAheadIndex < input.length ) {

            if (input[myLookAheadIndex] == '\n') {
                myLookAheadColumn = 1
                myLookAheadLine += 1
            } else {
                myLookAheadColumn += 1
            }

            myLookAheadIndex += 1

        }

    }

    /**
     * Advances the position of this tokenizer then returns the next look ahead character.
     */
    fun advanceAndLookAhead(): Char {
        advance()
        return lookAhead()
    }

    /**
     * Advances the position of this tokenizer then extracts a token from marked position up to (but not including)
     * the look ahead position.
     * @return the token extracted.
     */
    fun advanceAndExtractTokenFromMark(tokenType: ELZeroTokenType) : LZeroToken {
        advance()
        return extractTokenFromMark(tokenType)
    }

    /**
     * Extracts a token from marked position up to (but not including) the look ahead position. Sets the text of the
     * token as that substring and the line and column of the token as the marked line and column. Gives the token
     * the given [tokenType].
     * @return the token extracted.
     */
    fun extractTokenFromMark(tokenType: ELZeroTokenType) =
        LZeroToken(
            tokenType,
            input.substring(myMarkedIndex, myLookAheadIndex),
            myMarkedLine,
            myMarkedColumn
        )

    /**
     * Returns the text of the token that would be extracted by a call to extractTokenFromMark.
     * @return the text of the potential token to be extracted.
     */
    fun extractedTokenText() =
        input.substring(myMarkedIndex, myLookAheadIndex)

    /**
     * @return the next not-yet-read character from the input string.
     */
    fun lookAhead(): Char =
        if (myLookAheadIndex < input.length) input[myLookAheadIndex]
        else END_OF_INPUT_CHAR

    /**
     * Sets the marked position to be the same as the look-ahead position. I.e. the next not-yet-read character
     * will become the first character of the next not-yet-scanned token.
     */
    fun mark() {
        myMarkedColumn = myLookAheadColumn
        myMarkedIndex = myLookAheadIndex
        myMarkedLine = myLookAheadLine
    }

    /**
     * Sets the marked position to be the same as the look-ahead position then advances the look ahead one character.
     */
    fun markAndAdvance() {
        mark()
        advance()
    }

    ////

    companion object {

        /**
         * Pseudo character representing the end of input, i.e. the virtual character one index past the end of
         * the input string.
         */
        val END_OF_INPUT_CHAR: Char = 0.toChar()

    }

}

//---------------------------------------------------------------------------------------------------------------------

