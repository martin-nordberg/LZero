//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connecteds

import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectedQualifiedName(
    val qualifiedName : LZeroName
) : LZeroConnected( qualifiedName.origin)

//---------------------------------------------------------------------------------------------------------------------

