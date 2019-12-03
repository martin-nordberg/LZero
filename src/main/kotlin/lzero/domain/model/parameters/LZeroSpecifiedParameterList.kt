//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.parameters

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroSpecifiedParameterList(
    origin: LZeroOrigin,
    val parameters: List<LZeroParameter>
) : LZeroParameterList(origin) {

    override fun writeCode(output: CodeWriter) =
        output.writeParenCommaBlock(parameters) { p ->
            p.writeCode(this)
        }

}

//---------------------------------------------------------------------------------------------------------------------

