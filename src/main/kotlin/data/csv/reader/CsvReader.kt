package org.example.data.csv.reader

import org.example.core.domain.exception.InvalidFileNameException
import org.example.data.csv.parser.CsvParser
import org.example.data.csv.helper.FileName
import java.io.File

class CsvReader<T>(private val parser: CsvParser<T>){
    suspend fun read(fileName: String): List<T>{
        if (fileName !in FileName.allFiles)
            throw InvalidFileNameException()
        val filePath = "src/main/kotlin/$fileName"
        val file = File(filePath)
        if (file.exists()){
            val csvLines = file.readLines()
            return parser.parseFile(csvLines)
        }else
            return emptyList()
    }
}