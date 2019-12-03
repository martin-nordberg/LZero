//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.annotations

import lzero.domain.generating.CodeWriter
import lzero.domain.model.arguments.LZeroArgumentList
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroAnnotation(
    val origin: LZeroOrigin,
    val qualifiedName: LZeroName,
    val arguments: LZeroArgumentList
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    fun writeCode(output: CodeWriter) {
        output.write( "@", qualifiedName.text )
        arguments.writeCode(output)
    }

}

//---------------------------------------------------------------------------------------------------------------------

