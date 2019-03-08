//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.model.annotations.LZeroAnnotationList
import lzero.domain.model.connections.LZeroConnectionList
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.names.LZeroName
import lzero.domain.model.uuids.LZeroUuid

//---------------------------------------------------------------------------------------------------------------------

class LZeroElement(
    val documentation: LZeroDocumentation,
    val annotations: LZeroAnnotationList,
    val concept: LZeroConcept,
    val qualifiedName: LZeroName,
    val uuid: LZeroUuid,
    val connections: LZeroConnectionList
) {

    val origin: LZeroOrigin = concept.origin

}

//---------------------------------------------------------------------------------------------------------------------

