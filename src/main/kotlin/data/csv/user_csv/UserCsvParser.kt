package org.example.data.csv.user_csv

import CsvParser
import org.example.data.csv.smartCsvSplit
import org.example.models.Role
import org.example.models.User
import java.util.*

class UserCsvParser: CsvParser<User> {
    override fun parseLine(line: String): User? {

        return runCatching {
            val trimmedLine = line.trim()
            if (trimmedLine.isBlank()){
                throw  IllegalArgumentException("Error : Empty CSV Line.")
            }
                val fields = smartCsvSplit(trimmedLine).map { it.trim().removeSurrounding("[","]").removeSurrounding("\"") }
                    val stringIdToUUID = UUID.fromString(fields[UserColumnIndex.ID])
                    val name = fields[UserColumnIndex.NAME]
                    val password = fields[UserColumnIndex.PASSWORD]
                    val email = fields[UserColumnIndex.EMAIL]
                    val role = Role.valueOf(fields[UserColumnIndex.ROLE])
                    val isDeleted = fields[UserColumnIndex.IS_DELETED].toBoolean()
                if (stringIdToUUID == UUID(0, 0) || name.isBlank() || password.isBlank() || email.isBlank() || role.toString().isBlank()) {
                    throw IllegalArgumentException("Invalid Data")
                }
                 User(
                    id = stringIdToUUID,
                    name = name,
                    password = password,
                    email = email,
                    role = role,
                    isDeleted = isDeleted
                )
        }.fold(
            onSuccess = {
                user -> user
            },
            onFailure = { exception ->
                println("Error while parsing line: ${exception.message}")
                return null
            }
        )
    }

    override fun parseFile(csvLines: List<String>): List<User> {
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }


}