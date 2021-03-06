//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.expressions.literals.LZeroLiteralExpression

//---------------------------------------------------------------------------------------------------------------------

class LZeroLiteralValueAssignment(
    origin: LZeroOrigin,
    val expression: LZeroLiteralExpression
) : LZeroValueAssignment(origin) {

    override fun writeCode(output: CodeWriter) {
        output.write("= ")
        expression.writeCode(output)
    }

}

//---------------------------------------------------------------------------------------------------------------------

