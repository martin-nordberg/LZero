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

        // Scan an identifier.
        if (isIdentifierStart(nextChar)) {
            return scanIdentifier()
        }

        // Scan a block of documentation.
        if (nextChar == '/' && input.lookAhead() == '*') {
            input.advance()
            return scanDocumentation()
        }

        // Scan a concept keyword.
        if (nextChar == '#') {
            return scanConceptKeyword()
        }

        // Scan a connector keyword.
        if (nextChar == '~') {
            return scanConnectorKeyword()
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
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_CHARACTER_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.CHARACTER_LITERAL)

    }

    /**
     * Scans a block of documentation after its opening '/' and '*' characters have been marked and consumed in
     * the tokenizer.
     */
    private fun scanDocumentation(): LZeroToken {

        var nextChar = input.lookAhead()

        while(true) {

            if (nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_DOCUMENTATION)
            }

            input.advance()

            if (nextChar == '*' && input.lookAhead() == '/' ) {
                return input.advanceAndExtractTokenFromMark(ELZeroTokenType.DOCUMENTATION)
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

        return input.extractTokenFromMark(ELZeroTokenType.FLOATING_POINT_LITERAL)

    }

    /**
     * Scans an identifier after its first character has been marked and consumed.
     */
    private fun scanIdentifier(): LZeroToken {

        while (isIdentifierPart(input.lookAhead())) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.IDENTIFIER)

    }

    /**
     * Scans a concept keyword after its opening '#' character has been marked and consumed.
     */
    private fun scanConceptKeyword(): LZeroToken {

        if (isKeywordStart(input.lookAhead())) {
            input.advance()
        } else {
            return input.extractTokenFromMark(ELZeroTokenType.HASH)
        }

        while (isKeywordPart(input.lookAhead())) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.CONCEPT_KEYWORD)

    }

    /**
     * Scans a connector keyword after its opening '~' character has been marked and consumed.
     */
    private fun scanConnectorKeyword(): LZeroToken {

        if (isKeywordStart(input.lookAhead())) {
            input.advance()
        } else {
            return input.extractTokenFromMark(ELZeroTokenType.TILDE)
        }

        while (isKeywordPart(input.lookAhead())) {
            input.advance()
        }

        return input.extractTokenFromMark(ELZeroTokenType.CONNECTOR_KEYWORD)

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

        return input.extractTokenFromMark(ELZeroTokenType.INTEGER_LITERAL)

    }

    /**
     * Scans a backtick-quoted identifier after the open "`" character has been marked and consumed.
     */
    private fun scanQuotedIdentifier(): LZeroToken {

        var nextChar = input.lookAhead()

        while (nextChar != '`') {

            if (nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_QUOTED_IDENTIFIER)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.IDENTIFIER)

    }

    /**
     * Scans a string literal after the opening double quote character has been marked and consumed.
     */
    private fun scanStringLiteral(): LZeroToken {

        var nextChar = input.lookAhead()

        while (nextChar != '"') {

            if (nextChar == '\n' || nextChar == StringTokenizer.END_OF_INPUT_CHAR) {
                return input.extractTokenFromMark(ELZeroTokenType.UNTERMINATED_STRING_LITERAL)
            }

            nextChar = input.advanceAndLookAhead()

        }

        return input.advanceAndExtractTokenFromMark(ELZeroTokenType.STRING_LITERAL)

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
            return input.extractTokenFromMark(ELZeroTokenType.PERCENT)
        }

        val scanned = readUuidChars(7) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(4) && readDash() &&
                readUuidChars(12)

        if (scanned) {
            return input.extractTokenFromMark(ELZeroTokenType.UUID)
        }

        while (readUuidChars(1) || readDash()) {
            // keep consuming
        }

        return input.extractTokenFromMark(ELZeroTokenType.INVALID_UUID_LITERAL)

    }

    ////

    companion object {

        /** Characters that distinguish a floating point literal from an integer literal. */
        private val FLOATING_POINT_STARTERS = setOf('.', 'd', 'D', 'e', 'E', 'f', 'F')

        /** Characters that serve as tokens of length one. */
        private val ONE_CHARACTER_TOKENS = mapOf(
            ':' to ELZeroTokenType.COLON,
            ',' to ELZeroTokenType.COMMA,
            '-' to ELZeroTokenType.DASH,
            '.' to ELZeroTokenType.DOT,
            '=' to ELZeroTokenType.EQ,
            '{' to ELZeroTokenType.LEFT_BRACE,
            '[' to ELZeroTokenType.LEFT_BRACKET,
            '(' to ELZeroTokenType.LEFT_PARENTHESIS,
            '}' to ELZeroTokenType.RIGHT_BRACE,
            ']' to ELZeroTokenType.RIGHT_BRACKET,
            ')' to ELZeroTokenType.RIGHT_PARENTHESIS,
            ';' to ELZeroTokenType.SEMICOLON,
            StringTokenizer.END_OF_INPUT_CHAR to ELZeroTokenType.END_OF_INPUT
        )

    }

}

//---------------------------------------------------------------------------------------------------------------------

