//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.generating.CodeWriter
import lzero.domain.model.connectedelements.LZeroConnectedElement

//---------------------------------------------------------------------------------------------------------------------

class LZeroExplicitConnection(
    val connector: LZeroConnector,
    val connectedElement: LZeroConnectedElement
) : LZeroConnection(connector.origin) {

    override fun writeCode(output: CodeWriter) {
        connector.writeCode(output)
        connectedElement.writeCode(output)
    }

}

//---------------------------------------------------------------------------------------------------------------------

