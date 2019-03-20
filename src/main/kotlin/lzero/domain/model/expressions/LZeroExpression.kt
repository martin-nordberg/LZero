//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.expressions

import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.elements.LZeroElement

//---------------------------------------------------------------------------------------------------------------------

abstract class LZeroExpression(
    origin: LZeroOrigin,
    documentation: LZeroDocumentation
) : LZeroElement(origin, documentation) {

    abstract val text: String

}

//---------------------------------------------------------------------------------------------------------------------

