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
                writer.write("${user.id},${user.name},${user.password},${user.role},${user.isDeleted}")
            }
            writer.close()
            Result.success(Unit)
        }catch (e :Exception){
            Result.failure(e)
        }
    }

}