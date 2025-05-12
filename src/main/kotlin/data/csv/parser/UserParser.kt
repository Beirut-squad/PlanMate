package org.example.data.csv.parser

import data.exception.EmptyCSVFileException
import data.exception.InvalidDataFileException
import domain.exception.handler.ExceptionHandler
import domain.model.Role
import domain.model.User
import org.example.data.csv.helper.smartCsvSplit
import org.example.data.csv.column_index.UserColumnIndex
import java.util.*

class UserParser(
    private val exceptionHandler: ExceptionHandler
) : CsvParser<User> {
    override suspend fun parseLine(line: String): User {
        return exceptionHandler.tryCatchingAsyncWithResult(
            action = {
                val trimmedLine = line.trim()
                if (trimmedLine.isBlank()) {
                    throw EmptyCSVFileException()
                }
                val fields =
                    smartCsvSplit(trimmedLine).map { it.trim().removeSurrounding("[", "]").removeSurrounding("\"") }
                val stringIdToUUID = UUID.fromString(fields[UserColumnIndex.ID])
                val name = fields[UserColumnIndex.NAME]
                val password = fields[UserColumnIndex.PASSWORD]
                val email = fields[UserColumnIndex.EMAIL]
                val role = Role.valueOf(fields[UserColumnIndex.ROLE])
                val isDeleted = fields[UserColumnIndex.IS_DELETED].toBoolean()
                if (stringIdToUUID == UUID(
                        0,
                        0
                    ) || name.isBlank() || password.isBlank() || email.isBlank() || role.toString().isBlank()
                ) {
                    throw InvalidDataFileException()
                }
                User(
                    id = stringIdToUUID,
                    name = name,
                    password = password,
                    email = email,
                    role = role,
                    isDeleted = isDeleted
                )

            }
        )
    }

    override suspend fun parseFile(csvLines: List<String>): List<User> {
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }


}