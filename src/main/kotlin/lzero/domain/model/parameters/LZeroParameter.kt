//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.parameters

import lzero.domain.generating.CodeWriter
import lzero.domain.model.connections.LZeroImplicitConnection
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroSimpleName

//---------------------------------------------------------------------------------------------------------------------

class LZeroParameter(
    val origin: LZeroOrigin,
    val name: LZeroSimpleName,
    val connection: LZeroImplicitConnection
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    fun writeCode(output: CodeWriter) {
        output.write(name.text)
        connection.writeCode(output)
    }

}

//---------------------------------------------------------------------------------------------------------------------

