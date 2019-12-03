//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.expressions.LZeroExpression

//---------------------------------------------------------------------------------------------------------------------

class LZeroArgument(
    val origin: LZeroOrigin,
    val name: LZeroArgumentName,
    val expression: LZeroExpression
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    fun writeCode(output: CodeWriter) {

        if (name is LZeroSpecifiedArgumentName) {
            output.write( name.text, " = ")
        }

        expression.writeCode(output)

    }

}

//---------------------------------------------------------------------------------------------------------------------

