//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

//---------------------------------------------------------------------------------------------------------------------

enum class ELZeroTokenType(
    val text: String
) {

    /* Keywords */
    CONCEPT_KEYWORD("[concept]"),
    CONNECTOR_KEYWORD("[connector]"),

    /* Identifiers */
    IDENTIFIER("[identifier]"),

    /* Punctuation */
    COLON("':'"),
    COMMA("','"),
    DASH("'-'"),
    DOT("'.'"),
    EQ("'='"),
    HASH("'#'"),
    LEFT_BRACE("'{'"),
    LEFT_BRACKET("'['"),
    LEFT_PARENTHESIS("'('"),
    PERCENT("'%'"),
    RIGHT_BRACE("'}'"),
    RIGHT_BRACKET("'}'"),
    RIGHT_PARENTHESIS("')'"),
    SEMICOLON("';'"),
    TILDE("'~'"),

    /* Literals */
    CHARACTER_LITERAL("[character literal]"),
    FLOATING_POINT_LITERAL("[floating point literal]"),
    INTEGER_LITERAL("[integer literal]"),
    STRING_LITERAL("[string literal]"),
    UUID( "[UUID]" ),

    /* Documentation */
    DOCUMENTATION( "[documentation]" ),

    /** Errors */
    INVALID_CHARACTER( "[invalid character]" ),
    INVALID_UUID_LITERAL( "[invalid UUID literal]" ),
    UNTERMINATED_CHARACTER_LITERAL( "[unterminated character literal]" ),
    UNTERMINATED_DOCUMENTATION( "[unterminated documentation]" ),
    UNTERMINATED_QUOTED_IDENTIFIER( "[unterminated quoted identifier]" ),
    UNTERMINATED_STRING_LITERAL( "[unterminated string literal]" ),

    /** End of input. */
    END_OF_INPUT( "[end of input]");

    ////

    override fun toString(): String {
        return text
    }

}

//---------------------------------------------------------------------------------------------------------------------

