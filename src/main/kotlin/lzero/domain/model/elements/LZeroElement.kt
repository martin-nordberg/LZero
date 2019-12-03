//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroElement(
    val origin: LZeroOrigin,
    val documentation: LZeroDocumentation
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    abstract fun writeCode(output: CodeWriter)

}

//---------------------------------------------------------------------------------------------------------------------

