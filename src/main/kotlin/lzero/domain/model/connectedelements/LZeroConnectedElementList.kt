//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connectedelements

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.elements.LZeroElement

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectedElementList(
    origin: LZeroOrigin,
    val connectedElements: List<LZeroElement>
) : LZeroConnectedElement(origin) {

    override fun writeCode(output: CodeWriter) =
        output.writeSqBracketCommaBlock( connectedElements) { e ->
            e.writeCode(this)
        }

}

//---------------------------------------------------------------------------------------------------------------------

