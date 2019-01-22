//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package i.lzero.domain.scanning

//---------------------------------------------------------------------------------------------------------------------

class CharBuffer(
    private val input: String
) {

    private var myLookAheadColumn = 1

    private var myLookAheadIndex = 0

    private var myLookAheadLine: Int = 1

    private var myMarkedColumn = 1

    private var myMarkedIndex = 0

    private var myMarkedLine: Int = 1

    ////

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

    fun advanceAndLookAhead(): Char {
        advance()
        return lookAhead()
    }

    fun advanceAndExtractTokenFromMark(tokenType: ELZeroTokenType) : LZeroToken {
        advance()
        return extractTokenFromMark(tokenType)
    }

    fun extractTokenFromMark(tokenType: ELZeroTokenType) =
        LZeroToken(
            tokenType,
            input.substring(myMarkedIndex, myLookAheadIndex),
            myMarkedLine,
            myMarkedColumn
        )

    fun lookAhead(): Char =
        if (myLookAheadIndex < input.length) input[myLookAheadIndex]
        else END_OF_INPUT_CHAR

    fun lookAhead(nCharacters: Int): Char =
        if (myLookAheadIndex + nCharacters <= input.length) input[myLookAheadIndex + nCharacters - 1]
        else END_OF_INPUT_CHAR

    fun mark() {
        myMarkedColumn = myLookAheadColumn
        myMarkedIndex = myLookAheadIndex
        myMarkedLine = myLookAheadLine
    }

    fun markAndAdvance() {
        mark()
        advance()
    }

    ////

    companion object {
        val END_OF_INPUT_CHAR: Char = 0.toChar()
    }

}

//---------------------------------------------------------------------------------------------------------------------

