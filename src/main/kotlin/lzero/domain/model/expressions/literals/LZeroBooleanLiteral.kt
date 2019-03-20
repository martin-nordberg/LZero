//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.expressions.literals

import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.documentation.LZeroDocumentation

//---------------------------------------------------------------------------------------------------------------------

class LZeroBooleanLiteral(
    origin: LZeroOrigin,
    documentation: LZeroDocumentation,
    override val text: String
) : LZeroLiteralExpression(origin, documentation)

//---------------------------------------------------------------------------------------------------------------------

