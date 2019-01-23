//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import java.lang.Character.*

//---------------------------------------------------------------------------------------------------------------------

/**
 * Scanner for L-Zero code. Orchestrates the given [input] tokenizer to produce the individual tokens of a string
 * of raw L-Zero code.
 */
class LZeroScanner(
    private val input: StringTokenizer
) {

    /**
     * Reads the next token from the input.
     */
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

        if ( nextChar == '!' ) {
            return scanDocumentationLine()
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

        if (nextChar == '`') {
            return scanQuotedIdentifier()
        }

        return input.extractTokenFromMark(ELZeroTokenType.INVALID_CHARACTER)

    }

    ////

    /**
     * @return true if the given [character] can be part of an identifier (after its first character).
     */
    private fun isIdentifierPart(character: Char) =
        isJavaIdentifierPart(character) && character != StringTokenizer.END_OF_INPUT_CHAR

    /**
     * @return true if the given [character] can be the first character of an identifier.
     */
    private fun isIdentifierStart(character: Char) =
        isJavaIdentifierStart(character) && character != StringTokenizer.END_OF_INPUT_CHAR

    /**
     * @return true if the given [character] can be part of a keyword (after its opening '#' character).
     */
    private fun isKeywordPart(character: Char) =
        isJavaIdentifierPart(character) && character != StringTokenizer.END_OF_INPUT_CHAR

    /**
     * Scans a character literal token after its opening "'" character has been marked and consumed in the tokenizer.
     */
    private fun scanCharacterLiteral() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( nextChar != '\'' ) {

            if ( nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR ) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.CHARACTER_LITERAL)

    }

    /**
     * Scans a line of documentation after its opening "!" character has been marked and consumed in the tokenizer.
     */
    private fun scanDocumentationLine() : LZeroToken {

        var nextChar = input.advanceAndLookAhead()

        while ( nextChar != '\n' && nextChar != StringTokenizer.END_OF_INPUT_CHAR ) {
            nextChar = input.advanceAndLookAhead()
        }

        return input.extractTokenFromMark(ELZeroTokenType.DOCUMENTATION_LINE)

    }

    /**
     * Scans a floating point literal after marking the first digit and consuming up until (but not including) the
     * first character seen that distinguishes it from an integer literal.
     */
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

    /**
     * Scans an identifier after its first character has been marked and consumed.
     */
    private fun scanIdentifier() : LZeroToken {

        while ( isIdentifierPart( input.lookAhead() ) ) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.IDENTIFIER)

    }

    /**
     * Scans a keyword after its opening '#' character has been marked and consumed.
     */
    private fun scanKeyword() : LZeroToken {

        var atLeastOneCharacterSeen = false

        while ( isKeywordPart( input.lookAhead() ) ) {
            input.advance()
            atLeastOneCharacterSeen = true
        }

        if ( !atLeastOneCharacterSeen ) {
            return input.extractTokenFromMark(ELZeroTokenType.EMPTY_KEYWORD)
        }

        return input.extractTokenFromMark(ELZeroTokenType.KEYWORD)

    }

    /**
     * Scans a numerical literal (integer or floating point) after its first numeric digit has been marked and consumed.
     */
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

    /**
     * Scans a backtick-quoted identifier after the open "`" character has been marked and consumed.
     */
    private fun scanQuotedIdentifier() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( nextChar != '`' ) {

            if ( nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR ) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.IDENTIFIER)

    }

    /**
     * Scans a string literal after the opening double quote character has been marked and consumed.
     */
    private fun scanStringLiteral() : LZeroToken {

        var nextChar = input.lookAhead()

        while ( nextChar != '"' ) {

            if ( nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR ) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_STRING_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.STRING_LITERAL)

    }

    ////

    companion object {

        /** Characters that distinguish a floating point literal from an integer literal. */
        private val FLOATING_POINT_STARTERS = setOf( '.', 'd', 'D', 'e', 'E', 'f', 'F' )

        /** Characters that serve as tokens of length one. */
        private val ONE_CHARACTER_TOKENS = mapOf(
            ':' to ELZeroTokenType.COLON,
            ',' to ELZeroTokenType.COMMA,
            '.' to ELZeroTokenType.DOT,
            '=' to ELZeroTokenType.EQ,
            '{' to ELZeroTokenType.LBRACE,
            '(' to ELZeroTokenType.LPAREN,
            '}' to ELZeroTokenType.RBRACE,
            ')' to ELZeroTokenType.RPAREN,
            StringTokenizer.END_OF_INPUT_CHAR to ELZeroTokenType.END_OF_INPUT
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

