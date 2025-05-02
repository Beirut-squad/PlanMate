package org.example.data.csv

import CsvParser
import data.csv.FileName
import java.io.File

class CsvReader<T>(private val parser: CsvParser<T>){
    fun read(fileName: String): List<T>{
        if (fileName !in FileName.allFiles)
            throw Exception("File $fileName cannot be found in file names")
        val filePath = "src/main/kotlin/$fileName"
        val file = File(filePath)
        if (!file.exists())
            throw Exception("File $fileName cannot be found in $filePath ")
        val csvLines = file.readLines()
        return  parser.parseFile(csvLines)
    }
}