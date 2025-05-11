package org.example.data.csv.writer

interface CsvWriter<T> {
    suspend fun writeToFile(items: List<T>, filePath: String)
}