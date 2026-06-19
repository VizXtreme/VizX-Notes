fun String.withHardLineBreaks(): String = buildString(length + length / 2) {
    val content = this@withHardLineBreaks
    var inFencedBlock = false
    var pendingBlanks = 0
    var isFirst = true
    var i = 0

    fun flushBlanks() {
        append("\n\n")
        repeat(maxOf(0, pendingBlanks - 1)) { append("\u00A0\n\n") }
        pendingBlanks = 0
    }

    while (i <= content.length) {
        val eol = content.indexOf('\n', i).let { if (it == -1) content.length else it }
        var isFence = false
        var j = i
        while (j < eol && content[j] == ' ') j++
        if (j + 3 <= eol && content[j] == '`' && content[j + 1] == '`' && content[j + 2] == '`') {
            isFence = true
        }

        if (inFencedBlock) {
            append('\n')
            append(content, i, eol)
            if (isFence) inFencedBlock = false
        } else if (i == eol) {
            pendingBlanks++
        } else {
            if (!isFirst) {
                appendLine()
                if (pendingBlanks > 0) flushBlanks() else append('\n')
            }
            isFirst = false
            append(content, i, eol)
            if (isFence) inFencedBlock = true
        }

        i = eol + 1
    }

    if (pendingBlanks > 0) flushBlanks()
}

fun main() {
    val input = "Line 1\nLine 2\n\nLine 4\n| Table | Row |\n|---|---|\n| 1 | 2 |"
    println("INPUT:")
    println(input)
    println("\nOUTPUT:")
    println(input.withHardLineBreaks())
}
