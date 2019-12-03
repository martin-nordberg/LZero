//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.elements.LZeroElement

//---------------------------------------------------------------------------------------------------------------------

class LZeroContainment(
    origin: LZeroOrigin,
    val containedElements: List<LZeroElement>
) : LZeroConnection(origin) {

    override fun writeCode(output: CodeWriter) =
        output.writeBraceSemicolonBlock(containedElements) { e ->
            e.writeCode(this)
        }

}

//---------------------------------------------------------------------------------------------------------------------

