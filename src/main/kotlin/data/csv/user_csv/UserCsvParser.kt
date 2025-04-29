package org.example.data.csv.user_csv

import CsvParser
import org.example.models.Role
import org.example.models.User
import java.util.*

class UserCsvParser: CsvParser<User> {
    override fun parseLine(line: String): User? {
        if (line.isBlank()) {
            println("Error: Empty CSV line.")
            return null
        }
        val fields = line.split(",").map { it.trim() }

        val idStr = UUID.fromString(fields[0])
        val name = fields[1]
        val password = fields[2]
        val email =fields[3]
        val role = Role.valueOf(fields[4])
        if (idStr.toString().isBlank() || name.isBlank() || password.isBlank() || email.isBlank() || role.toString().isBlank()) {
            println("Error: Missing or empty field(s) in CSV line.")
            return null
        }
        return try {
            User(
                id = idStr,
                name = name,
                password = password,
                email = email,
                role = role,
                isDeleted = fields[5].toBoolean()
            )
        } catch (e :Exception){
            println("Error while parsing user $e")
           return null
        }
    }

    override fun parseFile(csvLines: List<String>): List<User> {
        TODO("Not yet implemented")
    }
}