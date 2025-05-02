package org.example.data.csv

import java.io.File

interface CsvWriter<T> {
    fun writeToFile(items: List<T>, filePath: String): Result<Unit>
}