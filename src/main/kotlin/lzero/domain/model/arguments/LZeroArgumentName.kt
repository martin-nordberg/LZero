//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroArgumentName(
    val origin: LZeroOrigin
) {

    abstract val text: String

}

//---------------------------------------------------------------------------------------------------------------------

