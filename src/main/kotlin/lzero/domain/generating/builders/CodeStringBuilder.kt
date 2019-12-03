//
// (C) Copyright 2018 Martin E. Nordberg III
// Apache 2.0 License
//

package lzero.domain.generating.builders

//---------------------------------------------------------------------------------------------------------------------

/**
 * Wraps a string builder to support indentation and trial output that is line length limited.
 */
class CodeStringBuilder(
    private val indentWhiteSpace: String = "  ",
    private val maxLineLength: Int = 100
) {

    /** The code output so far, except for the latest line under construction. */
    private val builder = StringBuilder()

    /** The code of the last line being output. */
    private val currentLine = StringBuilder()

    /** The current indentation level. */
    private var indentationLevel = 0

    /** The white space corresponding to the current indentation level. */
    private var indentSpaces = ""

    /** If greater than zero, a line exceeding maxLineLength will trigger a LineLengthExceededException. */
    private var lineLengthLimitedCount = 0;

    ////

    /**
     * Moves the current line from its holding point to the string builder output.
     */
    private fun acceptCurrentLine() {

        // Capture the current line from its builder.
        var code = currentLine.toString().trimEnd(' ', '\t')
        currentLine.clear()

        // Deal with optional line breaks.
        if (code.contains(SOFT_HYPHEN)) {

            if (currentLine.length + code.length > maxLineLength) {
                if (lineLengthLimitedCount > 0) {
                    throw LineLengthExceededException()
                }
                append(code.replace(SOFT_HYPHEN, "\n"))
                return
            }

            code = code.replace(SOFT_HYPHEN, "")

        }

        if (lineLengthLimitedCount > 0 && code.length > maxLineLength) {
            throw LineLengthExceededException()
        }

        // Move the current line into the finished output.
        builder.append(code)

    }

    /**
     * Appends a given [snippet] to the output code. Handles indentation for new lines.
     */
    fun append(snippet: String) {

        var lineStartIndex = 0
        var lineEndIndex = snippet.indexOf('\n')

        while (true) {

            // Once no new line characters remain, complete simpler output then quit.
            if (lineEndIndex < 0) {
                val remainder = snippet.substring(lineStartIndex)
                appendToCurrentLine(remainder)
                break
            }

            // Determine the next line of output.
            val line = snippet.substring(lineStartIndex, lineEndIndex)

            // Move things from the current line to the main string builder; clear out the current line.
            appendToCurrentLine(line)

            // Move the current line into the finished output.
            acceptCurrentLine()
            builder.append('\n')

            // Advance to the next line.
            lineStartIndex = lineEndIndex + 1
            lineEndIndex = snippet.indexOf('\n', lineStartIndex)

        }

    }

    /**
     * Appends an explicit new line to the output.
     */
    fun appendNewLine() =
        append("\n")

    /**
     * Appends a [fragment] of code (known to have no new line characters) to the current line.
     */
    private fun appendToCurrentLine(fragment: String) {

        // Indent if the line is empty and the added code is not.
        if (currentLine.isEmpty() && fragment.trimEnd(' ', '\t').isNotEmpty()) {
            currentLine.append(indentSpaces)
            currentLine.append(fragment.trimStart(' ', '\t'))
        }
        else {
            currentLine.append(fragment)
        }

        if (lineLengthLimitedCount > 0 && currentLine.length > maxLineLength) {
            throw LineLengthExceededException()
        }

    }

    /**
     * Increments the indentation level by one for output occurring after this call.
     */
    private fun indent() {
        indentationLevel += 1
        indentSpaces += indentWhiteSpace
    }

    /**
     * Executes the given [output] with the indentation level increased by one.
     * @param output the closure to call for indented output.
     */
    fun indented(output: CodeStringBuilder.() -> Unit) {
        this.indent()
        try {
            this.output()
        }
        finally {
            this.unindent()
        }
    }

    fun lineLengthLimited(writeCode: CodeStringBuilder.() -> Unit): Boolean {

        // Mark where we started from
        val lengthBeforeTrial = builder.length
        val currentLineBeforeTrial = currentLine.toString()

        try {
            // try writing the code with line length limited
            this.lineLengthLimitedCount += 1
            this.writeCode()
        }
        catch (e: LineLengthExceededException) {
            // Restore our output to its original condition
            builder.setLength(lengthBeforeTrial)
            currentLine.clear()
            currentLine.append(currentLineBeforeTrial)

            // Return failure.
            return false
        }
        finally {
            this.lineLengthLimitedCount -= 1
        }

        return true
    }

    /**
     * Converts the code builder output to a simple string.
     */
    override fun toString(): String {

        var result = builder.toString()

        // TODO: this does not account for soft hyphen behavior in last/current line
        if (currentLine.length > 0) {
            result += currentLine.toString().trimEnd(' ', '\t')
        }

        return result

    }

    /**
     * Decrements the indentation level by one.
     */
    private fun unindent() {

        check(this.indentationLevel > 0)

        indentationLevel -= 1

        indentSpaces = ""
        for (i in 1..indentationLevel) {
            indentSpaces += indentWhiteSpace
        }

        if (currentLine.isBlank()) {
            currentLine.clear()
        }

    }

    ////

    companion object {

        /** Represents a place to put an optional line break in order to reduce line width; otherwise omitted. */
        val SOFT_HYPHEN = "\u00AD"

    }

}

//---------------------------------------------------------------------------------------------------------------------

