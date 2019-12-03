//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.chunks

import lzero.domain.generating.builders.CodeStringBuilder

//---------------------------------------------------------------------------------------------------------------------


class NewLineCodeChunk
    : ICodeChunk {

    override val hasNestedBlocks = false

    override fun writeCode(output: CodeStringBuilder) {
        output.appendNewLine()
    }

}
