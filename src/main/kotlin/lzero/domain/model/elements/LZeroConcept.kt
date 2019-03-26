//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroConcept(
    val origin: LZeroOrigin,
    val qualifiedName: LZeroName
) {

    val text =
        "#" + qualifiedName.text

}

//---------------------------------------------------------------------------------------------------------------------

