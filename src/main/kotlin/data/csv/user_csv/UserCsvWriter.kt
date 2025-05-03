package org.example.data.csv.user_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.User
import java.io.BufferedWriter
import java.io.File
import java.util.*

class UserCsvWriter : CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String): Result<Unit> {
        kotlin.runCatching {
            val file = File("src/main/kotlin/$filePath")
            if (!isValidFileName(file.name))
                throw IllegalArgumentException("Error: Invalid file name")
            //val writer = BufferedWriter(FileWriter(file))
//            if (file.length() == 0L)
//                writer.write("[id,name,password,email,role,isDeleted]\n")
            if (items.isNotEmpty())
                writeUser(items, file)
        }.fold(
            onSuccess = { return Result.success(Unit) },
            onFailure = { return Result.failure(it) }
        )
    }

    private fun writeUser(items: List<User>, file: File) {
        items.forEach { user ->
            if (isValidUser(user))
                file.writeText("[${user.id},${user.name},${user.password},${user.email},${user.role},${user.isDeleted}]\n")
        }
    }

    internal fun isValidUser(user: User): Boolean {
        return user.id != UUID(0, 0) && user.name.isNotBlank() && user.password.isNotBlank() && user.email.isNotBlank()
    }
}