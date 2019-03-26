//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.parameters

import lzero.domain.model.connections.LZeroConnectionList
import lzero.domain.model.core.LZeroOrigin
import lzero.domain.model.names.LZeroSimpleName

//---------------------------------------------------------------------------------------------------------------------

class LZeroParameter(
    val origin: LZeroOrigin,
    val name: LZeroSimpleName,
    val connectionList: LZeroConnectionList
) {

    val text = name.text + connectionList.text

}

//---------------------------------------------------------------------------------------------------------------------

