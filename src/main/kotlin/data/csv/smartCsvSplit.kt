package org.example.data.csv

 fun smartCsvSplit(line: String): List<String> {
    val fields = mutableListOf<String>()
    var current = StringBuilder()
     var insideQuotes = false
    var bracketDepth = 0

    for (char in line) {
        when{
             char == '"' -> {
                insideQuotes = !insideQuotes
                current.append(char)
            }
            char == '[' -> {
                bracketDepth++
                current.append(char)
            }
            char == ']' -> {
                bracketDepth--
                current.append(char)
            }
            char == ',' && !insideQuotes && bracketDepth == 0  -> {
                    fields.add(current.toString().trim())
                    current = StringBuilder()
            }
            else -> current.append(char)
        }
    }
    fields.add(current.toString().trim()) // add last field
    return fields
}
