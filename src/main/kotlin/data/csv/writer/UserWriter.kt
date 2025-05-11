package org.example.data.csv.writer

import domain.model.User
import org.example.data.csv.helper.isValidFileName
import java.io.File
import java.util.*

class UserWriter : CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String): Result<Unit> {
        runCatching {
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Error: Invalid file name")
            writeUser(items, file)
        }.fold(
            onSuccess = { return Result.success(Unit) },
            onFailure = { return Result.failure(it) }
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