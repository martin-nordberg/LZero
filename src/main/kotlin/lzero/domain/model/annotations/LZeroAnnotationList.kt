//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.annotations

import lzero.domain.model.core.LZeroNullOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroAnnotationList(
    val annotations: List<LZeroAnnotation>
) {

    val origin =
        if (annotations.isNotEmpty()) annotations[0].origin else LZeroNullOrigin

}

//---------------------------------------------------------------------------------------------------------------------

