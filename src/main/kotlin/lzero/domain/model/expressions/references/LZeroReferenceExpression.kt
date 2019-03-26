//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.expressions.references

import lzero.domain.model.arguments.LZeroArgumentList
import lzero.domain.model.connections.LZeroValueAssignment
import lzero.domain.model.documentation.LZeroDocumentation
import lzero.domain.model.expressions.LZeroExpression
import lzero.domain.model.names.LZeroName
import lzero.domain.model.names.LZeroNullName
import lzero.domain.model.uuids.LZeroUuid

//---------------------------------------------------------------------------------------------------------------------

class LZeroReferenceExpression(
    documentation: LZeroDocumentation,
    val qualifiedName: LZeroName,
    val uuid: LZeroUuid,
    val arguments: LZeroArgumentList,
    val valueAssignment: LZeroValueAssignment
) : LZeroExpression(
    if (qualifiedName is LZeroNullName) uuid.origin else qualifiedName.origin,
    documentation
) {

    override val text: String =
        "TODO"
}

//---------------------------------------------------------------------------------------------------------------------

