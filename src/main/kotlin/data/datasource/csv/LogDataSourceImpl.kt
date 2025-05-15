package data.datasource.csv

import data.datasource.csv.helper.FileName
import data.datasource.csv.reader.CsvReader
import data.datasource.csv.writer.CsvWriter
import data.datasource.LogDataSource
import ui.common.exception.NoProjectLogsFoundException
import ui.common.exception.NoTaskLogsFoundException
import domain.model.ProjectLog
import domain.model.TaskLog
import java.util.*

class LogDataSourceImpl(
    private val csvProjectLogReader: CsvReader<ProjectLog>,
    private val csvProjectLogWriter: CsvWriter<ProjectLog>,
    private val csvTaskLogReader: CsvReader<TaskLog>,
    private val csvTaskLogWriter: CsvWriter<TaskLog>,
) : LogDataSource {

    override suspend fun getProjectLogs(id: UUID): List<ProjectLog> {
        return csvProjectLogReader
            .read(FileName.PROJECT_LOG)
            .filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override suspend fun getTaskLogs(id: UUID): List<TaskLog> {
        return csvTaskLogReader
            .read(FileName.TASK_LOG)
            .filter { it.entityId == id }
            .takeIf { it.isNotEmpty() }
            ?: throw NoTaskLogsFoundException()
    }

    override suspend fun saveProjectLog(projectLog: ProjectLog) {
        val logs = csvProjectLogReader.read(FileName.PROJECT_LOG).toMutableList()
        logs.add(projectLog)
        csvProjectLogWriter.writeToFile(
            items = logs,
            filePath = FileName.PROJECT_LOG
        )
    }

    override suspend fun saveTaskLog(taskLog: TaskLog) {
        val logs = csvTaskLogReader.read(FileName.TASK_LOG).toMutableList()
        logs.add(taskLog)
        csvTaskLogWriter.writeToFile(
            items = logs,
            filePath = FileName.TASK_LOG
        )
    }

    override suspend fun getAllProjectLogs(): List<ProjectLog> {
        return csvProjectLogReader
            .read(FileName.PROJECT_LOG)
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectLogsFoundException()
    }

    override suspend fun getAllTaskLogs(): List<TaskLog> {
        return csvTaskLogReader
            .read(FileName.TASK_LOG)
            .takeIf { it.isNotEmpty() }
            ?: throw NoTaskLogsFoundException()
    }
}
