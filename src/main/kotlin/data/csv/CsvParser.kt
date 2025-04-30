import org.example.models.User

interface CsvParser<T> {

    fun parseLine(line: String): T?
    fun parseFile(csvLines: List<String>): List<T>
}