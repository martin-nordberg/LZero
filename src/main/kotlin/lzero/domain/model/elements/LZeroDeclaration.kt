//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.model.annotations.LZeroAnnotationList
import lzero.domain.model.connections.LZeroConnectionList
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.names.LZeroName
import lzero.domain.model.names.LZeroNullName
import lzero.domain.model.parameters.LZeroParameterList
import lzero.domain.model.uuids.LZeroKnownUuid
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
) : LZeroElement(concept.origin, documentation) {

    override val text: String
        get() {

            var result: String = documentation.text

            if (result.isNotEmpty()) {
                result += "\n"
            }

            result += annotations.annotations.joinToString(" ") { a -> a.text }

            if (annotations.annotations.isNotEmpty()) {
                result += "\n"
            }

            result += concept.text

            if (qualifiedName !is LZeroNullName) {
                result += " " + qualifiedName.text
            }

            if (uuid is LZeroKnownUuid) {
                result += " " + uuid.text
            }

            result += parameters.text

            result += connections.text

            return result

        }

}

//---------------------------------------------------------------------------------------------------------------------

