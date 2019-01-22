//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package i.lzero.domain.scanning

import java.lang.Character.*

//---------------------------------------------------------------------------------------------------------------------

class LZeroScanner(
    private val input: CharBuffer
) {

    fun scan() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( isWhitespace(nextChar) ) {
            nextChar = input.advanceAndLookAhead()
        }

        input.markAndAdvance()

        if ( ONE_CHARACTER_TOKENS.containsKey(nextChar) ) {
            return input.extractTokenFromMark(ONE_CHARACTER_TOKENS.get(nextChar)!!)
        }

        if (isIdentifierStart(nextChar)) {
            return scanIdentifier()
        }

        if ( nextChar == '#' ) {
            return scanKeyword()
        }

        if ( nextChar == '"' ) {
            return scanStringLiteral()
        }

        if ( nextChar == '\'' ) {
            return scanCharacterLiteral()
        }

        if ( isDigit(nextChar) ) {
            return scanNumericLiteral()
        }

        if ( nextChar == '!' ) {
            return scanDocumentationLine()
        }

        if ( nextChar == CharBuffer.END_OF_INPUT_CHAR ) {
            input.mark()
            return input.extractTokenFromMark(ELZeroTokenType.END_OF_INPUT)
        }

        // TODO: add line & column
        throw IllegalArgumentException( "Unrecognized character: '$nextChar'.")

    }

    ////

    private fun isIdentifierPart(nextChar: Char) =
        isJavaIdentifierPart(nextChar) && nextChar != CharBuffer.END_OF_INPUT_CHAR

    private fun isIdentifierStart(nextChar: Char) =
        isJavaIdentifierStart(nextChar) && nextChar != CharBuffer.END_OF_INPUT_CHAR

    private fun isKeywordPart(nextChar: Char) =
        isJavaIdentifierPart(nextChar) && nextChar != CharBuffer.END_OF_INPUT_CHAR

    private fun scanCharacterLiteral() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( nextChar != '\'' ) {

            if ( nextChar == '\n' ) {
                // TODO: more info
                throw IllegalArgumentException( "Character literal extends past end of line." )
            }

            if ( nextChar == CharBuffer.END_OF_INPUT_CHAR ) {
                throw IllegalArgumentException( "Character literal extends to end of input." )
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.CHARACTER_LITERAL)

    }

    private fun scanDocumentationLine() : LZeroToken {

        var nextChar = input.advanceAndLookAhead()

        while ( nextChar != '\n' && nextChar != CharBuffer.END_OF_INPUT_CHAR ) {
            nextChar = input.advanceAndLookAhead()
        }

        return input.extractTokenFromMark(ELZeroTokenType.DOCUMENTATION_LINE)

    }

    private fun scanFloatingPointLiteral() : LZeroToken {

        var nextChar = input.lookAhead()

        if ( nextChar == '.' ) {

            nextChar = input.advanceAndLookAhead()

            while ( isDigit(nextChar) || '_' == nextChar ) {
                nextChar = input.advanceAndLookAhead()
            }

        }

        if ( nextChar == 'e' || nextChar == 'E' ) {

            nextChar = input.advanceAndLookAhead()

            if ( nextChar == '-' || nextChar == '+' ) {
                nextChar = input.advanceAndLookAhead()
            }

            while ( isDigit(nextChar) || '_' == nextChar ) {
                nextChar = input.advanceAndLookAhead()
            }

        }

        if ( nextChar == 'd' || nextChar=='D' || nextChar == 'f' || nextChar=='F') {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.FLOATING_POINT_LITERAL)

    }

    private fun scanIdentifier() : LZeroToken {

        while ( isIdentifierPart( input.lookAhead() ) ) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.IDENTIFIER)

    }

    private fun scanKeyword() : LZeroToken {

        while ( isKeywordPart( input.lookAhead() ) ) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.KEYWORD)

    }

    private fun scanNumericLiteral() : LZeroToken {

        var nextChar = input.lookAhead()
        while ( isDigit( nextChar ) || '_' == nextChar ) {
            nextChar = input.advanceAndLookAhead()
        }

        if ( FLOATING_POINT_STARTERS.contains(nextChar) ) {
            return scanFloatingPointLiteral()
        }

        // TODO: hexadecimal

        // TODO: binary

        if ( nextChar == 'l' || nextChar=='L') {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.INTEGER_LITERAL)

    }

    private fun scanStringLiteral() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( nextChar != '"' ) {

            if ( nextChar == '\n' ) {
                // TODO: more info
                throw IllegalArgumentException( "String literal extends past end of line." )
            }

            if ( nextChar == CharBuffer.END_OF_INPUT_CHAR ) {
                throw IllegalArgumentException( "String literal extends to end of input." )
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.STRING_LITERAL)

    }

    ////

    companion object {

        private val FLOATING_POINT_STARTERS = setOf( '.', 'd', 'D', 'e', 'E', 'f', 'F' )

        private val ONE_CHARACTER_TOKENS = mapOf(
            ':' to ELZeroTokenType.COLON,
            ',' to ELZeroTokenType.COMMA,
            '.' to ELZeroTokenType.DOT,
            '=' to ELZeroTokenType.EQ,
            '{' to ELZeroTokenType.LBRACE,
            '(' to ELZeroTokenType.LPAREN,
            '}' to ELZeroTokenType.RBRACE,
            ')' to ELZeroTokenType.RPAREN
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

