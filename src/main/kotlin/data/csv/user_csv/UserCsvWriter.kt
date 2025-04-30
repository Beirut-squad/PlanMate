package org.example.data.csv.user_csv

import org.example.data.csv.CsvWriter
import org.example.models.User
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class UserCsvWriter: CsvWriter<User> {
    override fun writeToFile(items: List<User>, filePath: String): Result<Unit> {

        return try {
            val file = File(filePath)
            val writer = BufferedWriter(FileWriter(file))

            writer.write("id,name,password,email,role,isDeleted\n")

            for (user in items){
                if (isValidUser(user)){
                    writer.write("${user.id},${user.name},${user.password},${user.role},${user.isDeleted}\n")
                }
            }
            writer.close()
            Result.success(Unit)
        }catch (e :Exception){
            Result.failure(e)
        }
    }

    private fun isValidUser(user : User): Boolean{
        return user.name.isNotBlank() && user.id.toString().isNotBlank() && user.password.isNotBlank() && user.email.isNotBlank() && user.isDeleted != null
    }

}