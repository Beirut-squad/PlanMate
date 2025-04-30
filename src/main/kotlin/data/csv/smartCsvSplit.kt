package org.example.data.csv

private fun smartCsvSplit(line: String): List<String> {
    val fields = mutableListOf<String>()
    var current = StringBuilder()
    var bracketDepth = 0

    for (char in line) {
        when (char) {
            '[' -> {
                bracketDepth++
                current.append(char)
            }
            ']' -> {
                bracketDepth--
                current.append(char)
            }
            ',' -> {
                if (bracketDepth == 0) {
                    fields.add(current.toString().trim())
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }
            else -> current.append(char)
        }
    }
    fields.add(current.toString().trim()) // add last field
    return fields
}
