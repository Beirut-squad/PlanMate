package org.example.data.csv.helper

     fun smartCsvSplit(input: String): List<String> {
        val cleanedLine = input.removeSurrounding("[", "]")
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var bracketDepth = 0

        for (char in cleanedLine) {
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
                        result.add(current.toString().trim())
                        current.clear()
                    } else {
                        current.append(char)
                    }
                }

                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result.filter { it.isNotEmpty() }
    }
