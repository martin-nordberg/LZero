//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.connecteds.LZeroConnected

//---------------------------------------------------------------------------------------------------------------------

class LZeroExplicitConnection(
    val connector: LZeroConnector,
    val connected: LZeroConnected
) : LZeroConnection(connector.origin)

//---------------------------------------------------------------------------------------------------------------------

