package org.example.data.datasource.log_data_source

import data.csv.FileName.PROJECT_LOG
import data.csv.FileName.TASK_LOG
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.logic.exceptions.log_exceptions.NoProjectLogsFoundException
import org.example.logic.exceptions.log_exceptions.NoTaskLogsFoundException
import org.example.models.ProjectLog
import org.example.models.TaskLog
import java.util.UUID

class LogDataSourceImpl(
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
