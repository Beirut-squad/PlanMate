package data.datasource.csv.writer

interface CsvWriter<T> {
    suspend fun writeToFile(items: List<T>, filePath: String)
}