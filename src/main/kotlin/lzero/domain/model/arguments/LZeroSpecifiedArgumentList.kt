//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.arguments

import lzero.domain.model.core.LZeroOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroSpecifiedArgumentList(
    origin: LZeroOrigin,
    val arguments: List<LZeroArgument>
) : LZeroArgumentList(origin) {

    override val text =
        arguments.joinToString(", ", "(", ")") { a -> a.text }

}

//---------------------------------------------------------------------------------------------------------------------

