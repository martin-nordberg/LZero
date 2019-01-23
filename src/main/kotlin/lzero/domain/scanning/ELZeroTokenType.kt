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
    KEYWORD("[keyword]"),

    /* Identifiers */
    IDENTIFIER("[identifier]"),

    /* Punctuation */
    COLON("':'"),
    COMMA("','"),
    DOT("'.'"),
    EQ("'='"),
    LBRACE("'{'"),
    LPAREN("'('"),
    RBRACE("'}'"),
    RPAREN("')'"),

    /* Literals */
    CHARACTER_LITERAL("[character literal]"),
    FLOATING_POINT_LITERAL("[floating point literal]"),
    INTEGER_LITERAL("[integer literal]"),
    STRING_LITERAL("[string literal]"),

    /* Documentation */
    DOCUMENTATION_LINE( "[documentation]" ),

    /** Errors */
    EMPTY_KEYWORD( "[empty keyword]" ),
    INVALID_CHARACTER( "[invalid character]" ),
    UNTERMINATED_CHARACTER_LITERAL( "[unterminated character literal]" ),
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

