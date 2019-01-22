//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package i.lzero.domain.scanning

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

    END_OF_INPUT( "[end of input]");

    ////

    override fun toString(): String {
        return text
    }

}

//---------------------------------------------------------------------------------------------------------------------

