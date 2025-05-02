package org.example.data.csv.user_csv

import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.User
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

class UserCsvWriter: CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String): Result<Unit> {
        return kotlin.runCatching {
                val file = File(filePath)
                if (!isValidFileName(file.name)){
                    throw IllegalArgumentException("Error: Invalid file name")
                }
                val writer = BufferedWriter(FileWriter(file,true))
                if(file.length() == 0L){
                    writer.write("[id,name,password,email,role,isDeleted]\n")
                }
                    if (items.isNotEmpty()) for (user in items){
                        if (isValidUser(user)){
                            writer.write("[${user.id},${user.name},${user.password},${user.role},${user.isDeleted}]\n")
                        }
                    }
                    writer.close()

        }.fold(
            onSuccess = {
                return Result.success(Unit)
            },
            onFailure = {
                return Result.failure(it)
            }
        )

    }

    internal fun isValidUser(user : User): Boolean{
            return user.id !=UUID(0,0) && user.name.isNotBlank() && user.password.isNotBlank() && user.email.isNotBlank()

    }


}