package org.example.data.csv

import CsvParser
import java.io.File

class CsvReader<T>(private val parser: CsvParser<T>){

    fun read(fileName: String): List<T>{
        val csvLines = File(fileName).readLines()
        return csvLines.mapNotNull { line ->
            parser.parseLine(line)
        }
    }
}