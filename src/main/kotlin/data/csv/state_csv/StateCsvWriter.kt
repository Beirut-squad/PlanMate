package org.example.data.csv.state_csv

import org.example.data.csv.CsvWriter
import org.example.models.State
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class StateCsvWriter: CsvWriter<State> {
    override fun writeToFile(items: List<State>, filePath: String): Result<Unit> {

     return try{
         val file = File(filePath)
         val writer = BufferedWriter(FileWriter(file))
         return Result.success(Unit)

     }catch (e:Exception){
         return Result.failure(e)
     }

    }
}