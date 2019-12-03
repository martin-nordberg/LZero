//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroNullOrigin
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectionList(
    val connections: List<LZeroConnection> = listOf()
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    val origin: LZeroOrigin =
        if (connections.isNotEmpty()) connections[0].origin
        else LZeroNullOrigin

    fun writeCode(output: CodeWriter) {

        if ( !connections.isEmpty() ) {
            output.write(" ")
        }

        output.writeBlankSeparated(connections) { c ->
            c.writeCode(this)
        }

    }

}

//---------------------------------------------------------------------------------------------------------------------

