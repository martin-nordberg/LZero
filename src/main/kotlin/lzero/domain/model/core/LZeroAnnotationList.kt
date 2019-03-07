//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroAnnotationList(
    val annotations : List<LZeroAnnotation>
) {

    val origin: LZeroOrigin = if ( annotations.isNotEmpty() ) annotations[0].origin else LZeroNullOrigin()

}

//---------------------------------------------------------------------------------------------------------------------

