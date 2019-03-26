//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.connectedelements.LZeroConnectedElement
import lzero.domain.model.parameters.LZeroParameterList

//---------------------------------------------------------------------------------------------------------------------

class LZeroExplicitConnection(
    val connector: LZeroConnector,
    val parameters: LZeroParameterList,
    val connectedElement: LZeroConnectedElement
) : LZeroConnection(connector.origin) {

    override val text = "TBD"

}

//---------------------------------------------------------------------------------------------------------------------

