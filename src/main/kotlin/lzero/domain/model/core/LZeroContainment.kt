//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroContainment(
    origin: LZeroOrigin,
    val containedElements: List<LZeroElement>
) : LZeroConnection(origin)

//---------------------------------------------------------------------------------------------------------------------

