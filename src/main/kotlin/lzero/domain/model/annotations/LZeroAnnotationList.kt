//
// (C) Copyright 2019 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.model.annotations

import lzero.domain.generating.CodeWriter
import lzero.domain.model.core.LZeroNullOrigin

//---------------------------------------------------------------------------------------------------------------------

class LZeroAnnotationList(
    val annotations: List<LZeroAnnotation>
) {

    val code: String
        get() {
            val output = CodeWriter()
            writeCode(output)
            return output.toString()
        }

    fun isEmpty() =
        annotations.isEmpty()

    fun isNotEmpty() =
        annotations.isNotEmpty()

    fun writeCode(output: CodeWriter) =
        output.writeBlankSeparated(annotations) { a ->
            a.writeCode(this)
        }

    val origin =
        if (annotations.isNotEmpty()) annotations[0].origin else LZeroNullOrigin

}

//---------------------------------------------------------------------------------------------------------------------

