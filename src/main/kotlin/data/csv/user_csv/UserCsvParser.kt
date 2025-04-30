package org.example.data.csv.user_csv

import CsvParser
import org.example.models.Role
import org.example.models.User
import java.util.*

class UserCsvParser: CsvParser<User> {
    override fun parseLine(line: String): User? {

        val trimmedLine = line.trim()
        if (line.isBlank()) {
            println("Error: Empty CSV line.")
            return null
        }
        val regex = "\"([^\"]*)\"|([^,]+)".toRegex()
        val fields = regex.findAll(trimmedLine)
            .map { it.groupValues.drop(1).filter { it.isNotEmpty() } }
            .flatten()
            .toList()

        try {
            val idToString = UUID.fromString(fields[UserColumnIndex.ID])
            val name = fields[UserColumnIndex.NAME]
            val password = fields[UserColumnIndex.PASSWORD]
            val email = fields[UserColumnIndex.EMAIL]
            val role = Role.valueOf(fields[UserColumnIndex.ROLE])
            val isDeleted = fields[UserColumnIndex.IS_DELETED].toBoolean()
            if (name.isBlank() || password.isBlank() || email.isBlank() || role.toString()
                    .isBlank() || isDeleted.toString().isBlank()
            ) {
                println("Error: Missing or empty field(s) in CSV line.")
                return null
            } else {
                return try {
                    User(
                        id = idToString,
                        name = name,
                        password = password,
                        email = email,
                        role = role,
                        isDeleted = isDeleted
                    )
                } catch (e: Exception) {
                    println("Error while parsing user $e")
                    return null
                }
            }

        } catch (e: IllegalArgumentException) {
            println("Invalid field format: ${e.message}")
            return null

        }
    }

    override fun parseFile(csvLines: List<String>): List<User> {
        return csvLines.mapNotNull { parseLine(it) }
    }
}