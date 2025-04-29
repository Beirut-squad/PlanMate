package org.example.data.csv.user_csv

import CsvParser
import org.example.models.Role
import org.example.models.User
import java.util.*

class UserCsvParser: CsvParser<User> {
    override fun parseLine(line: String): User {
        TODO("Not yet implemented")
    }

    override fun parseFile(csvLines: List<String>): List<User> {
        TODO("Not yet implemented")
    }
}