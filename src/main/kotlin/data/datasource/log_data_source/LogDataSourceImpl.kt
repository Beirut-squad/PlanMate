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

    override fun getProjectLogs(id: UUID): Result<List<ProjectLog>> =
        runCatching {
            csvProjectLogReader.read(PROJECT_LOG).filter { it.entityId == id }
                .takeIf { it.isNotEmpty() }
                ?: throw NoProjectLogsFoundException()
        }

    override fun getTaskLogs(id: UUID): Result<List<TaskLog>> =
        runCatching {
            csvTaskLogReader.read(TASK_LOG).filter { it.entityId == id }
                .takeIf { it.isNotEmpty() }
                ?: throw NoTaskLogsFoundException()
        }

    override fun saveProjectLog(projectLog: ProjectLog): Result<Unit> =
        runCatching {
            val logs = csvProjectLogReader.read(PROJECT_LOG).toMutableList()
            logs.add(projectLog)
            csvProjectLogWriter.writeToFile(logs, PROJECT_LOG)
                .getOrThrow()
        }

    override fun saveTaskLog(taskLog: TaskLog): Result<Unit> =
        runCatching {
            val logs = csvTaskLogReader.read(TASK_LOG).toMutableList()
            logs.add(taskLog)
            csvTaskLogWriter.writeToFile(logs, TASK_LOG)
                .getOrThrow()
        }

    override fun getAllProjectLogs(): Result<List<ProjectLog>> =
        runCatching {
            val logs = csvProjectLogReader.read(PROJECT_LOG)
            if (logs.isEmpty()){
               emptyList()
            }else{
                logs
            }
        }

    override fun getAllTaskLogs(): Result<List<TaskLog>> =
        runCatching {
            csvTaskLogReader.read(TASK_LOG)
                .takeIf { it.isNotEmpty() }
                ?: throw NoTaskLogsFoundException()
        }
}
