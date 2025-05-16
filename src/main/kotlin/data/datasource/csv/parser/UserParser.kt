package data.datasource.csv.parser

import ui.common.exception.EmptyCSVFileException
import ui.common.exception.InvalidDataFileException
import domain.model.UserRole
import domain.model.User
import java.util.*

class UserParser : CsvParser<User> {
    override suspend fun parseLine(line: String): User {
        val trimmedLine = line.trim()
        if (trimmedLine.isBlank()) {
            throw EmptyCSVFileException()
        }
        val fields = data.datasource.csv.helper.smartCsvSplit(trimmedLine)
            .map { it.trim()
            .removeSurrounding("[", "]")
            .removeSurrounding("\"") }
        val stringIdToUUID = UUID.fromString(fields[data.datasource.csv.column_index.UserColumnIndex.ID])
        val name = fields[data.datasource.csv.column_index.UserColumnIndex.NAME]
        val password = fields[data.datasource.csv.column_index.UserColumnIndex.PASSWORD]
        val email = fields[data.datasource.csv.column_index.UserColumnIndex.EMAIL]
        val userRole = UserRole.valueOf(fields[data.datasource.csv.column_index.UserColumnIndex.ROLE])
        val isDeleted = fields[data.datasource.csv.column_index.UserColumnIndex.IS_DELETED].toBoolean()

        if (stringIdToUUID == UUID(0, 0)
            || name.isBlank()
            || password.isBlank()
            || email.isBlank()
            || userRole.toString().isBlank()
        ) {
            throw InvalidDataFileException()
        }
        return User(
            id = stringIdToUUID,
            name = name,
            password = password,
            email = email,
            userRole = userRole,
            isDeleted = isDeleted
        )
    }

    override suspend fun parseFile(csvLines: List<String>): List<User> {
        csvLines.drop(1)
        return csvLines.mapNotNull { parseLine(it) }
    }


}