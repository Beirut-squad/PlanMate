package org.example.data.csv.state_csv

import org.example.data.csv.CsvWriter
import org.example.models.State
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class StateCsvWriter: CsvWriter<State> {
    override fun writeToFile(items: List<State>, filePath: String): Result<Unit> {
     return runCatching {
         if (items.isEmpty()) {
             throw IllegalArgumentException("Empty state")
         }
         val file = File(filePath)
         val writer = BufferedWriter(FileWriter(file))

     }.fold(
         onSuccess = {
             return Result.success(Unit)
         },
         onFailure = {
             return Result.failure(it)
         }
     )

    }
}