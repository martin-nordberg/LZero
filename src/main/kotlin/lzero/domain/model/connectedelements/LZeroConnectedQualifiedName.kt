//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connectedelements

import lzero.domain.model.names.LZeroName

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectedQualifiedName(
    val qualifiedName: LZeroName
) : LZeroConnectedElement(qualifiedName.origin) {

    override val text =
        qualifiedName.text

}

//---------------------------------------------------------------------------------------------------------------------

