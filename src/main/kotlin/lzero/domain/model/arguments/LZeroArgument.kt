//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.expressions.LZeroExpression

//---------------------------------------------------------------------------------------------------------------------

class LZeroArgument(
    val origin: LZeroOrigin,
    val name: LZeroArgumentName,
    val expression: LZeroExpression
) {

    val text: String
        get() {

            var result = ""

            if (name is LZeroSpecifiedArgumentName) {
                result += name.text
                result += " = "
            }

            result += expression.text

            return result

        }
}

//---------------------------------------------------------------------------------------------------------------------

