//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.model.annotations.LZeroAnnotationList
import lzero.domain.model.connections.LZeroConnectionList
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.names.LZeroName
import lzero.domain.model.parameters.LZeroParameterList
import lzero.domain.model.uuids.LZeroUuid

//---------------------------------------------------------------------------------------------------------------------

class LZeroDeclaration(
    documentation: LZeroDocumentation,
    val annotations: LZeroAnnotationList,
    val concept: LZeroConcept,
    val qualifiedName: LZeroName,
    val uuid: LZeroUuid,
    val parameters: LZeroParameterList,
    val connections: LZeroConnectionList
) : LZeroElement(concept.origin, documentation)

//---------------------------------------------------------------------------------------------------------------------

