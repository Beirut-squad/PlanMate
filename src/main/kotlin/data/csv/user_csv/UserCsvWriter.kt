package org.example.data.csv.user_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.User
import java.io.File
import java.util.*

class UserCsvWriter : CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String) {
        try {
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Error: Invalid file name")
            writeUser(items, file)
        } catch (e: Exception) {
            println("Failed to write file: ${e.message}")
            throw e
        }
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