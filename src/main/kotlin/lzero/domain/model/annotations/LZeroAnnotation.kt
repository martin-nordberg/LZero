//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.annotations

import lzero.domain.model.arguments.LZeroArgumentList
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroAnnotation(
    val origin: LZeroOrigin,
    val qualifiedName: LZeroName,
    val argumentList: LZeroArgumentList
)

//---------------------------------------------------------------------------------------------------------------------

