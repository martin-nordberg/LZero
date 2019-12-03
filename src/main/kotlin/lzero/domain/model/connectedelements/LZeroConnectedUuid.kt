//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connectedelements

import lzero.domain.generating.CodeWriter
import lzero.domain.model.uuids.LZeroKnownUuid

//---------------------------------------------------------------------------------------------------------------------

class LZeroConnectedUuid(
    val uuid: LZeroKnownUuid
) : LZeroConnectedElement(uuid.origin) {

    override fun writeCode(output: CodeWriter) {
        output.write(uuid.text)
    }

}

//---------------------------------------------------------------------------------------------------------------------

