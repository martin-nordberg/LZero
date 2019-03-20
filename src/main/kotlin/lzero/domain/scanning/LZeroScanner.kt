//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

import lzero.domain.scanning.ELZeroTokenType.*
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
    fun scan(): LZeroToken {

        var nextChar = input.lookAhead()

        // Ignore whitespace.
        while (isWhitespace(nextChar)) {
            nextChar = input.advanceAndLookAhead()
        }

        // Consume the one character after marking the start of a token.
        input.markAndAdvance()

        if (ONE_CHARACTER_TOKENS.containsKey(nextChar)) {
            return input.extractTokenFromMark(ONE_CHARACTER_TOKENS.getValue(nextChar))
        }

        if (nextChar == '<' && input.lookAhead() == '~') {
            return input.advanceAndExtractTokenFromMark(LEFT_TILDE)
        }

        if (nextChar == '~') {

            if (input.lookAhead() == '>') {
                return input.advanceAndExtractTokenFromMark(RIGHT_TILDE)
            }

            return input.extractTokenFromMark(TILDE)

        }

        // Scan an identifier.
        if (isIdentifierStart(nextChar)) {
            return scanIdentifier()
        }

        // Scan a block of documentation.
        if (nextChar == '/' && input.lookAhead() == '*') {
            input.advance()
            return scanDocumentation()
        }

        // Scan a string literal.
        if (nextChar == '"') {
            return scanStringLiteral()
        }

        // Scan a character literal.
        if (nextChar == '\'') {
            return scanCharacterLiteral()
        }

        // Scan a numeric literal (integer or floating point).
        if (isDigit(nextChar)) {
            return scanNumericLiteral()
        }

        // Scan an identifier enclosed in back ticks.
        if (nextChar == '`') {
            return scanQuotedIdentifier()
        }

        // Scan a UUID.
        if (nextChar == '%') {
            return scanUuid()
        }

        // Error - nothing else it could be.
        return input.extractTokenFromMark(INVALID_CHARACTER)

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
     * @return true if the given [character] can be part of a keyword.
     */
    private fun isKeywordPart(character: Char) =
        isJavaIdentifierPart(character) && character != StringTokenizer.END_OF_INPUT_CHAR

    /**
     * @return true if the given [character] can be the first character of a keyword (after its opening '#' or '~'
     *         character).
     */
    private fun isKeywordStart(character: Char) =
        isJavaIdentifierStart(character) && character != StringTokenizer.END_OF_INPUT_CHAR

    /**
     * Scans a character literal token after its opening "'" character has been marked and consumed in the tokenizer.
     */
    private fun scanCharacterLiteral(): LZeroToken {

        var nextChar = input.lookAhead()

        while (nextChar != '\'') {

            if (nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(UNTERMINATED_CHARACTER_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(CHARACTER_LITERAL)

    }

    /**
     * Scans a block of documentation after its opening '/' and '*' characters have been marked and consumed in
     * the tokenizer.
     */
    private fun scanDocumentation(): LZeroToken {

        var nextChar = input.lookAhead()

        while (true) {

            if (nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(UNTERMINATED_DOCUMENTATION)
            }

            input.advance()

            if (nextChar == '*' && input.lookAhead() == '/') {
                return input.advanceAndExtractTokenFromMark(DOCUMENTATION)
            }

            nextChar = input.lookAhead()

        }

    }

    /**
     * Scans a floating point literal after marking the first digit and consuming up until (but not including) the
     * first character seen that distinguishes it from an integer literal.
     */
    private fun scanFloatingPointLiteral(): LZeroToken {

        var nextChar = input.lookAhead()

        if (nextChar == '.') {

            nextChar = input.advanceAndLookAhead()

            while (isDigit(nextChar) || '_' == nextChar) {
                nextChar = input.advanceAndLookAhead()
            }

        }

        if (nextChar == 'e' || nextChar == 'E') {

            nextChar = input.advanceAndLookAhead()

            if (nextChar == '-' || nextChar == '+') {
                nextChar = input.advanceAndLookAhead()
            }

            while (isDigit(nextChar) || '_' == nextChar) {
                nextChar = input.advanceAndLookAhead()
            }

        }

        if (nextChar == 'd' || nextChar == 'D' || nextChar == 'f' || nextChar == 'F') {
            input.advance()
        }

        return input.extractTokenFromMark(FLOATING_POINT_LITERAL)

    }

    /**
     * Scans an identifier after its first character has been marked and consumed.
     */
    private fun scanIdentifier(): LZeroToken {

        while (isIdentifierPart(input.lookAhead())) {
            input.advance()
        }

        val text = input.extractedTokenText()

        if (text == "true" || text == "false") {
            return input.extractTokenFromMark(BOOLEAN_LITERAL)
        }

        return input.extractTokenFromMark(IDENTIFIER)

    }

    /**
     * Scans a numerical literal (integer or floating point) after its first numeric digit has been marked and consumed.
     */
    private fun scanNumericLiteral(): LZeroToken {

        var nextChar = input.lookAhead()
        while (isDigit(nextChar) || '_' == nextChar) {
            nextChar = input.advanceAndLookAhead()
        }

        if (FLOATING_POINT_STARTERS.contains(nextChar)) {
            return scanFloatingPointLiteral()
        }

        // TODO: hexadecimal

        // TODO: binary

        if (nextChar == 'l' || nextChar == 'L') {
            input.advance()
        }

        return input.extractTokenFromMark(INTEGER_LITERAL)

    }

    /**
     * Scans a backtick-quoted identifier after the open "`" character has been marked and consumed.
     */
    private fun scanQuotedIdentifier(): LZeroToken {

        var nextChar = input.lookAhead()

        while (nextChar != '`') {

            if (nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(UNTERMINATED_QUOTED_IDENTIFIER)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(IDENTIFIER)

    }

    /**
     * Scans a string literal after the opening double quote character has been marked and consumed.
     */
    private fun scanStringLiteral(): LZeroToken {

        var nextChar = input.lookAhead()

        while (nextChar != '"') {

            if (nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(UNTERMINATED_STRING_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(STRING_LITERAL)

    }

    /**
     * Scans a UUID after the initial '%' character has been consumed.
     */
    private fun scanUuid(): LZeroToken {

        val uuidChars = "abcdefABCDEF1234567890"

        fun readUuidChars(nChars: Int): Boolean {
            for (i in 1..nChars) {
                val nextChar = input.lookAhead()
                if (!uuidChars.contains(nextChar)) {
                    return false
                }
                input.advance()
            }
            return true
        }

        fun readDash(): Boolean {
            val nextChar = input.lookAhead()
            if (nextChar == '-') {
                input.advance()
                return true
            }
            return false
        }

        if (!readUuidChars(1)) {
            return input.extractTokenFromMark(PERCENT)
        }

        val scanned = readUuidChars(7) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(12)

        if (scanned) {
            return input.extractTokenFromMark(UUID)
        }

        while (readUuidChars(1) || readDash()) {
            // keep consuming
        }

        return input.extractTokenFromMark(INVALID_UUID_LITERAL)

    }

    ////

    companion object {

        /** Characters that distinguish a floating point literal from an integer literal. */
        private val FLOATING_POINT_STARTERS = setOf('.', 'd', 'D', 'e', 'E', 'f', 'F')

        /** Characters that serve as tokens of length one. */
        private val ONE_CHARACTER_TOKENS = mapOf(
            ':' to COLON,
            ',' to COMMA,
            '-' to DASH,
            '.' to DOT,
            '=' to EQ,
            '#' to HASH,
            '{' to LEFT_BRACE,
            '[' to LEFT_BRACKET,
            '(' to LEFT_PARENTHESIS,
            '}' to RIGHT_BRACE,
            ']' to RIGHT_BRACKET,
            ')' to RIGHT_PARENTHESIS,
            ';' to SEMICOLON,
            StringTokenizer.END_OF_INPUT_CHAR to END_OF_INPUT
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

