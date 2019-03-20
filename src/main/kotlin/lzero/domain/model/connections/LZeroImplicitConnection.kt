//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.connectedelements.LZeroConnectedElement
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroImplicitConnection(
    origin: LZeroOrigin,
    val connectedElement: LZeroConnectedElement
) : LZeroConnection(origin)

//---------------------------------------------------------------------------------------------------------------------

