package data.datasource.log

import org.example.data.csv.helper.FileName.PROJECT_LOG
import org.example.data.csv.helper.FileName.TASK_LOG
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import domain.exception.log.NoProjectLogsFoundException
import domain.exception.log.NoTaskLogsFoundException
import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.UUID

class LogDataSourceImplementation(
    private val csvProjectLogReader: CsvReader<ProjectLog>,
    private val csvProjectLogWriter: CsvWriter<ProjectLog>,
    private val csvTaskLogReader: CsvReader<TaskLog>,
    private val csvTaskLogWriter: CsvWriter<TaskLog>,
) : LogDataSource {

    override fun getProjectLogs(id: UUID): List<ProjectLog> {
        return csvProjectLogReader.read(PROJECT_LOG).filter { it.entityId == id }.takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override fun getTaskLogs(id: UUID): List<TaskLog> {
        return csvTaskLogReader.read(TASK_LOG).filter { it.entityId == id }.takeIf { it.isNotEmpty() }
            ?: throw NoTaskLogsFoundException()
    }

    override fun saveProjectLog(projectLog: ProjectLog) {
        val logs = csvProjectLogReader.read(PROJECT_LOG).toMutableList()
        logs.add(projectLog)
        csvProjectLogWriter.writeToFile(logs, PROJECT_LOG)
    }

    override fun saveTaskLog(taskLog: TaskLog) {
        val logs = csvTaskLogReader.read(TASK_LOG).toMutableList()
        logs.add(taskLog)
        csvTaskLogWriter.writeToFile(logs, TASK_LOG)
    }

    override fun getAllProjectLogs(): List<ProjectLog> {
        return csvProjectLogReader.read(PROJECT_LOG)
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override fun getAllTaskLogs(): List<TaskLog> {
        return csvTaskLogReader.read(TASK_LOG)
            .takeIf { it.isNotEmpty() }
            ?: throw NoTaskLogsFoundException()
    }
}
