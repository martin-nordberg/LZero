//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.elements

import lzero.domain.generating.CodeWriter
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

    override fun writeCode(output: CodeWriter) {

        if (documentation.text.isNotEmpty()) {
            output.write(documentation.text)
            output.writeNewLine()
        }

        annotations.writeCode(output)

        if (annotations.isNotEmpty()) {
            output.writeNewLine()
        }

        output.write(concept.text)

        if (qualifiedName !is LZeroNullName) {
            output.write(" ", qualifiedName.text)
        }

        if (uuid is LZeroKnownUuid) {
            output.write(" ", uuid.text)
        }

        parameters.writeCode(output)

        connections.writeCode(output)

    }

}

//---------------------------------------------------------------------------------------------------------------------

