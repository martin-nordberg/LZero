//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.documentation

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroDocumentation(
    val origin: LZeroOrigin
) {

    abstract val text: String

}

//---------------------------------------------------------------------------------------------------------------------

