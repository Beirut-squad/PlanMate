package org.example.data.csv.user_csv

import org.example.data.csv.CsvWriter
import org.example.models.User

class UserCsvWriter: CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}