package org.example.data.csv.writer

interface CsvWriter<T> {
    fun writeToFile(items: List<T>, filePath: String): Result<Unit>
}