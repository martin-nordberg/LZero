//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.scanning

//---------------------------------------------------------------------------------------------------------------------

class LZeroToken(
    val type: ELZeroTokenType,
    val text: String,
    val line: Int,
    val column: Int,
    val value: Any = text
) {

    val length: Int
        get() = text.length

}

//---------------------------------------------------------------------------------------------------------------------

