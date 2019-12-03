//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.chunks

import lzero.domain.generating.builders.CodeStringBuilder

//---------------------------------------------------------------------------------------------------------------------


interface ICodeChunk {

    val hasNestedBlocks: Boolean

    fun writeCode(output: CodeStringBuilder)

}
