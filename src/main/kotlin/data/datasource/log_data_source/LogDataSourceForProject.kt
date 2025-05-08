package org.example.data.datasource.log_data_source

import org.example.models.ProjectLog

//**Remove** This class is not used anywhere.
interface LogDataSourceForProject {
    fun getHistory(): List<ProjectLog>
    fun createLog(log: ProjectLog)
}