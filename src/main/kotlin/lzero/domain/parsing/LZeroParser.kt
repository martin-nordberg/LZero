//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.parsing

import lzero.domain.model.core.*
import lzero.domain.scanning.ELZeroTokenType
import lzero.domain.scanning.LZeroToken

//---------------------------------------------------------------------------------------------------------------------

class LZeroParser(
    private val fileName: String,
    code: String
) {

    private val input = LZeroExpectedTokenBufferImpl(code)

    ////

    /**
     * element
     *   : documentation? annotations? KEYWORD IDENTIFIER? ( "(" parameters? ")" )? ( ":" type )? content
     *   ;
     */
    fun parseElement(): LZeroElement {

        // documentation?
        val documentation = parseDocumentation()

        // TODO: annotations

        // KEYWORD
        val keyword = parseKeyword()

        // TODO: identifier

        // TODO: parameters

        // TODO: type

        // TODO: content

        return LZeroElement(documentation, keyword)
    }

    /**
     * Parses the next token, which is expected to be a keyword.
     *
     * KEYWORD
     *   : "#" IDENTIFIER
     *   ;
     */
    private fun parseKeyword(): LZeroKeyword {

        val keywordToken = input.read(ELZeroTokenType.KEYWORD)

        return LZeroKeyword(keywordToken.origin, keywordToken.text)

    }

    /**
     * documentation
     *   : DOCUMENTATION_LINE+
     *   ;
     *
     * DOCUMENTATION_LINE
     *   : "!" [^\n]* "\n"
     *   ;
     */
    private fun parseDocumentation(): LZeroDocumentation {

        val documentationLines = mutableListOf<LZeroDocumentationLine>()

        while (input.hasLookAhead(ELZeroTokenType.DOCUMENTATION_LINE)) {
            val token = input.read()
            documentationLines.add(LZeroDocumentationLine(token.origin, token.text))
        }

        return LZeroDocumentation(documentationLines)

    }

    private val LZeroToken.origin
        get() = LZeroFileOrigin(fileName, this.line, this.column)

}

//---------------------------------------------------------------------------------------------------------------------

