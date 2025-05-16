package data.datasource.csv.reader

import ui.common.exception.InvalidFileNameException
import data.datasource.csv.helper.FileName
import data.datasource.csv.parser.CsvParser
import java.io.File

class CsvReader<T>(
    private val parser: CsvParser<T>
){
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