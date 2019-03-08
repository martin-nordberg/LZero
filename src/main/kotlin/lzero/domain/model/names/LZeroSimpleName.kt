//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.names

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroSimpleName(
    origin: LZeroOrigin,
    override val text : String
) : LZeroName(origin) {

}

//---------------------------------------------------------------------------------------------------------------------

