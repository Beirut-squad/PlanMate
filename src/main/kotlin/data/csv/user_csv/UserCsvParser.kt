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
            val idStr = UUID.fromString(fields[0])
            val name = fields[1]
            val password = fields[2]
            val email = fields[3]
            val role = Role.valueOf(fields[4])
            val isDeleted = fields[5].toBoolean()
            if (name.isBlank() || password.isBlank() || email.isBlank() || role.toString()
                    .isBlank() || isDeleted.toString().isBlank()
            ) {
                println("Error: Missing or empty field(s) in CSV line.")
                return null
            } else {
                return try {
                    User(
                        id = idStr,
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