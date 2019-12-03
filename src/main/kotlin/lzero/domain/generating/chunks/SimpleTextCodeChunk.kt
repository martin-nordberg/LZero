//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.chunks

import lzero.domain.generating.builders.CodeStringBuilder

//---------------------------------------------------------------------------------------------------------------------


class SimpleTextCodeChunk(
    private var code: String
) : ICodeChunk {

    override val hasNestedBlocks = false

    override fun writeCode(output: CodeStringBuilder) {
        output.append(code)
    }

}
