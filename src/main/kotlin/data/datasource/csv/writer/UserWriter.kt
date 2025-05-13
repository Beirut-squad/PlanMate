package data.datasource.csv.writer

import data.datasource.csv.helper.isValidFileName
import ui.common.exception.InvalidFileNameException
import domain.model.User
import java.io.File
import java.util.*

class UserWriter : CsvWriter<User> {
    override suspend fun writeToFile(items: List<User>, filePath: String) {
        val file = File("src/main/kotlin/$filePath")
        if (!isValidFileName(file.name)) throw InvalidFileNameException()
        writeUser(items, file)
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