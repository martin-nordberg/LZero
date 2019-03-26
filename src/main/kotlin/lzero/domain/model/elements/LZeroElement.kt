//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroElement(
    val origin: LZeroOrigin,
    val documentation: LZeroDocumentation
) {

    abstract val text: String

}

//---------------------------------------------------------------------------------------------------------------------

