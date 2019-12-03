//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.expressions.literals

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.expressions.LZeroExpression

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroLiteralExpression(
    origin: LZeroOrigin,
    documentation: LZeroDocumentation
) : LZeroExpression(origin, documentation) {

    abstract val text: String

    final override fun writeCode(output: CodeWriter) {
        output.write(text)
    }

}

//---------------------------------------------------------------------------------------------------------------------

