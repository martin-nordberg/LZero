//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.core.LZeroNullOrigin
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectionList(
    val connections : List<LZeroConnection> = listOf()
) {

    val origin: LZeroOrigin = if ( connections.isNotEmpty() ) connections[0].origin else LZeroNullOrigin

}

//---------------------------------------------------------------------------------------------------------------------

