//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.connections

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroNullOrigin

//---------------------------------------------------------------------------------------------------------------------

object LZeroNullValueAssignment :
    LZeroValueAssignment(LZeroNullOrigin) {

    override fun writeCode(output: CodeWriter) {}

}

//---------------------------------------------------------------------------------------------------------------------

