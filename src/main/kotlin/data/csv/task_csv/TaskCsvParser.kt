package org.example.data.csv.task_csv_parser

import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.models.Task
import java.util.*

class TaskCsvParser(

): TaskDataSource {
    override fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: UUID): Task {
        TODO("Not yet implemented")
    }
}