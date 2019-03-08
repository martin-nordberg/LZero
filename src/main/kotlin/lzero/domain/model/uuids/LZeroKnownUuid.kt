//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.uuids

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroKnownUuid(
    origin: LZeroOrigin,
    override val text : String
) : LZeroUuid( origin )

//---------------------------------------------------------------------------------------------------------------------

