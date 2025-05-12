package org.example.data.csv.writer

import data.exception.InvalidFileNameException
import domain.exception.handler.ExceptionHandler
import domain.model.User
import org.example.data.csv.helper.isValidFileName
import java.io.File
import java.util.*

class UserWriter(
    private val exceptionHandler: ExceptionHandler
) : CsvWriter<User> {
    override suspend fun writeToFile(items: List<User>, filePath: String) {

        exceptionHandler.tryCatchingAsync(
            action = {
                val file = File("src/main/kotlin/$filePath")
                if (!isValidFileName(file.name))
                    throw InvalidFileNameException()
                writeUser(items, file)
            }
        )
    }

    private fun writeUser(items: List<User>, file: File) {
        file.writeText("")
        items.forEach { user ->
            file.appendText("[${user.id},${user.name},${user.password},${user.email},${user.role},${user.isDeleted}]\n")
        }
    }

    internal fun isValidUser(user: User): Boolean {
        return user.id != UUID(0, 0) && user.name.isNotBlank() && user.password.isNotBlank() && user.email.isNotBlank()
    }
}