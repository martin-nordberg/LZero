//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.names

//---------------------------------------------------------------------------------------------------------------------

class LZeroQualifiedName(
    val names: List<LZeroSimpleName>
) : LZeroName(if (names.isNotEmpty()) names[0].origin else throw IllegalArgumentException("")) {

    override val text =
        names.joinToString(".") { n -> n.text }

}


//---------------------------------------------------------------------------------------------------------------------

