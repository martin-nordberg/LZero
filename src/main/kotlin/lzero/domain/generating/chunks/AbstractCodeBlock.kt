//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.chunks

import lzero.domain.generating.builders.CodeStringBuilder

//---------------------------------------------------------------------------------------------------------------------


abstract class AbstractCodeBlock() : ICodeChunk {

    private var codeChunks = mutableListOf<ICodeChunk>()

    override var hasNestedBlocks: Boolean = false

    ////

    fun add(codeChunk: ICodeChunk) {
        codeChunks.add(codeChunk)
        hasNestedBlocks = hasNestedBlocks || codeChunk !is SimpleTextCodeChunk && codeChunk !is SimpleCodeBlock
    }

    protected open fun getPrefix(density: ECodeDensity): String {
        return ""
    }

    abstract fun getSeparator(density: ECodeDensity): String

    protected open fun getSuffix(density: ECodeDensity): String {
        return ""
    }

    fun hasContent() =
        codeChunks.isNotEmpty()

    override fun writeCode(output: CodeStringBuilder) {

        if (hasNestedBlocks) {
            writeCode(output, ECodeDensity.NEW_LINE_PER_ITEM)
        }
        else {

            // First try all one line.
            var written = output.lineLengthLimited {
                writeCode(this, ECodeDensity.ALL_ONE_LINE)
            }

            // If too long, use extra line or nested and indented.
            if (!written) {
                written = output.lineLengthLimited {
                    writeCode(this, ECodeDensity.EXTRA_LINE)
                }

                if (!written) {
                    writeCode(output, ECodeDensity.NEW_LINE_PER_ITEM)
                }
            }

        }

    }

    private fun writeCode(output: CodeStringBuilder, density: ECodeDensity) {

        val prefix = getPrefix(density)
        val separator = getSeparator(density)
        val suffix = getSuffix(density)

        output.append(prefix)

        var sep = ""

        when (density) {

            ECodeDensity.ALL_ONE_LINE      ->
                for (codeChunk in codeChunks) {
                    output.append(sep)
                    codeChunk.writeCode(output)
                    sep = separator
                }

            ECodeDensity.EXTRA_LINE        -> {

                output.appendNewLine()

                output.indented {
                    for (codeChunk in codeChunks) {
                        output.append(sep)
                        codeChunk.writeCode(output)
                        sep = separator
                    }
                }

                output.appendNewLine()

            }

            ECodeDensity.NEW_LINE_PER_ITEM -> {

                if (hasNestedBlocks) {
                    output.appendNewLine()
                }

                output.indented {
                    for (codeChunk in codeChunks) {
                        output.append(sep)
                        output.appendNewLine()
                        codeChunk.writeCode(output)
                        sep = separator
                    }
                }

                if (hasNestedBlocks) {
                    output.appendNewLine()
                }

                output.appendNewLine()

            }

        }

        output.append(suffix)

    }

}
