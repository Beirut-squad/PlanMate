package org.example.data.csv

interface CsvWriter<T> {
    fun writeToFile(items: List<T> , filePath: String): Result<Unit>
}