//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroSpecifiedArgumentName(
    origin: LZeroOrigin,
    override val text: String
) : LZeroArgumentName(origin) {

}

//---------------------------------------------------------------------------------------------------------------------

