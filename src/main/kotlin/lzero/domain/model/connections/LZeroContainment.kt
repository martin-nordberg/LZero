//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.elements.LZeroElement
import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroContainment(
    origin: LZeroOrigin,
    val containedElements: List<LZeroElement>
) : LZeroConnection(origin)

//---------------------------------------------------------------------------------------------------------------------

