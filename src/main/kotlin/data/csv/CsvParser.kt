import org.example.models.Log

interface CsvParser<T> {

    fun parseLine(line: String): T
    fun parseFile(csvLines: List<String>): List<T>
}