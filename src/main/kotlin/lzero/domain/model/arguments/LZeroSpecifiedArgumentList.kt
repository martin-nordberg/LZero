//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroSpecifiedArgumentList(
    origin: LZeroOrigin,
    val arguments: List<LZeroArgument>
) : LZeroArgumentList(origin) {

    override fun writeCode(output: CodeWriter) =
        output.writeParenCommaBlock(arguments) { a ->
            a.writeCode(this)
        }

}

//---------------------------------------------------------------------------------------------------------------------

