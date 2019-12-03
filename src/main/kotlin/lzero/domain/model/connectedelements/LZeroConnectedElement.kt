//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connectedelements

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroConnectedElement(
    val origin: LZeroOrigin
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

