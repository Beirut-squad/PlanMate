package data.csv.project_csv
import org.example.data.csv.CsvWriter
import org.example.data.csv.isValidFileName
import org.example.models.Project
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.UUID
class ProjectCsvWriter: CsvWriter<Project> {
    override fun writeToFile(items: List<Project>, filePath: String): Result<Unit> {
        return runCatching {
            val file = File("src/main/kotlin/$filePath")
            if(!isValidFileName(file.name)){
                throw IllegalArgumentException("Error : Invalid file name")
            }
            if (items.isNotEmpty())
                for (project in items){
                    //if (isValidProject(project)){
                        file.writeText(
                            "[${project.id}," +
                                "${project.name}," +
                                "${project.description}," +
                                "${project.creatorUserID}," +
                                "${project.createdAt}," +
                                "${project.updatedAt}," +
                                "${project.state}]\n")
                    }
               // }
        }
    }
    private fun isValidProject(project : Project): Boolean{
        return project.id != UUID(0,0) && project.name.isNotBlank() && project.description.isNotBlank() && project.creatorUserID!= UUID(0,0)
    }
}