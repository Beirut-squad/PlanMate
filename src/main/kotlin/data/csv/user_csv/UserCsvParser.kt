package org.example.data.csv.user_csv

import CsvParser
import org.example.models.Role
import org.example.models.User
import java.util.*

class UserCsvParser: CsvParser<User> {
    override fun parseLine(line: String): User {
        val fields = line.split(",").map { it.trim() }
        return User(
            id = UUID.fromString(fields[0]),
            name = fields[1],
            password = fields[2],
            email = fields[3],
            role = Role.valueOf(fields[4]),
            isDeleted = fields[5].toBoolean()
        )
    }

    override fun parseFile(csvLines: List<String>): List<User> {
        TODO("Not yet implemented")
    }
}