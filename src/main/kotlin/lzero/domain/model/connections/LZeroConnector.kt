//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.model.arguments.LZeroArgumentList
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnector(
    val origin: LZeroOrigin,
    val qualifiedName: LZeroName,
    val arguments: LZeroArgumentList,
    val direction: ELZeroConnectionDirection
) {

    val text: String
        get() {
            var result = when (direction) {
                ELZeroConnectionDirection.LEFT -> " <~"
                else                           -> " ~"
            }

            result += qualifiedName.text

            result += arguments.text

            result += when (direction) {
                ELZeroConnectionDirection.RIGHT -> "~> "
                else                            -> "~ "
            }

            return result

        }
}

//---------------------------------------------------------------------------------------------------------------------

