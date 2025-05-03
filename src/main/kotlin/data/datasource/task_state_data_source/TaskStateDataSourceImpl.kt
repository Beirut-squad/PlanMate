package org.example.data.datasource.task_state_data_source

import data.datasource.task_state_data_source.TaskStateDataSource
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.models.TaskState
import java.util.UUID

class TaskStateDataSourceImpl(
    private val writer: CsvWriter<TaskState>,
    private val reader: CsvReader<TaskState>,
    private val filePath: String,
) : TaskStateDataSource {
    override fun createTaskState(taskState: TaskState): Result<TaskState> = runCatching {

        val states = reader.read(filePath)
        val newList = states + taskState
        writer.writeToFile(newList, filePath)
        taskState
    }

    override fun editTaskState(taskState: TaskState): Result<TaskState> = runCatching {

        val states = reader.read(filePath)
        val updatedStates = states.map {
            if (it.id == taskState.id) taskState else it
        }

        if (states.none { it.id == taskState.id }) {
            throw IllegalStateException("State not found")
        }

        writer.writeToFile(updatedStates, filePath)
        taskState
    }

    override fun deleteTaskState(id: UUID): Result<Unit> = runCatching {

        val states = reader.read(filePath)
        if (states.none { it.id == id }) {
            throw IllegalStateException("State not found")
        }

        val updatedStates = states.filterNot { it.id == id }
        writer.writeToFile(updatedStates, filePath)
    }
}

