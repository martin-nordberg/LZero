//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroElement(
    val documentation: LZeroDocumentation,
    val keyword: LZeroKeyword
) {

    val origin: LZeroOrigin = keyword.origin

}

//---------------------------------------------------------------------------------------------------------------------

