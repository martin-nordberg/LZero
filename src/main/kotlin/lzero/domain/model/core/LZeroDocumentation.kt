//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroDocumentation(
    val lines : List<LZeroDocumentationLine>
) {

    val origin: LZeroOrigin = if ( lines.isNotEmpty() ) lines[0].origin else LZeroNullOrigin()

}

//---------------------------------------------------------------------------------------------------------------------

