//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connecteds

import lzero.domain.model.uuids.LZeroKnownUuid

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectedUuid(
    val uuid : LZeroKnownUuid
) : LZeroConnected( uuid.origin)

//---------------------------------------------------------------------------------------------------------------------

