//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroQualifiedName(
    val names : List<LZeroName>
) {

    val origin: LZeroOrigin = if ( names.isNotEmpty() ) names[0].origin else LZeroNullOrigin()

}

//---------------------------------------------------------------------------------------------------------------------

