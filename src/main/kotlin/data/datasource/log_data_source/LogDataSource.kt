package org.example.data.datasource.log_data_source

import org.example.models.TaskLog

interface LogDataSource {
    fun getTasksHistory(): List<TaskLog>
    fun createTaskLog(taskLog: TaskLog)
    fun createProjectLog(taskLog: TaskLog)
}