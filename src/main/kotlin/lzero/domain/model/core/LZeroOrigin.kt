//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

sealed class LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroNullOrigin
    : LZeroOrigin() {

    override fun toString() =
        "[no origin]"

}

//---------------------------------------------------------------------------------------------------------------------

data class LZeroFileOrigin(
    val fileName: String,
    val line: Int,
    val column: Int
) : LZeroOrigin() {

    override fun toString() =
        "$fileName($line,$column)"

}

//---------------------------------------------------------------------------------------------------------------------

