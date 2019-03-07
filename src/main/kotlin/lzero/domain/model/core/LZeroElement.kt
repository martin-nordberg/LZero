//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.core

//---------------------------------------------------------------------------------------------------------------------

class LZeroElement(
    val documentation: LZeroDocumentation?,
    val annotations: LZeroAnnotationList,
    val concept: LZeroConcept,
    val qualifiedName: LZeroQualifiedName?,
    val uuid: LZeroUuid?,
    val connections: LZeroConnectionList
) {

    val origin: LZeroOrigin = concept.origin

}

//---------------------------------------------------------------------------------------------------------------------

