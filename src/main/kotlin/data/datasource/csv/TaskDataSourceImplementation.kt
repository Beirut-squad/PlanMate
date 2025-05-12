package data.datasource.csv

import data.exception.*
import domain.model.Task
import org.example.data.csv.helper.FileName
import org.example.data.csv.reader.CsvReader
import org.example.data.csv.writer.CsvWriter
import org.example.data.datasource.TaskDataSource
import java.util.UUID

class TaskDataSourceImplementation(
    private val csvReader: CsvReader<Task>,
    private val csvWriter: CsvWriter<Task>,
    private val taskFile: String = FileName.TASKS
) : TaskDataSource {


    override suspend fun createTask(task: Task) {
        val currentTasks = csvReader.read(taskFile)
        val updatedTasks = addTaskToList(currentTasks, task)
        if (updatedTasks.isNotEmpty()) csvWriter.writeToFile(updatedTasks, taskFile)
        else throw TaskCreationFailedException()

    }

    override suspend fun editTask(task: Task) {
        val currentTasks = csvReader.read(taskFile)
        validateTasksExist(currentTasks)

        val taskIndex = findTaskIndexById(currentTasks, task.id)
        validateTaskExists(taskIndex)

        val updatedTasks = updateTaskInList(currentTasks, taskIndex, task)

        if (updatedTasks.isNotEmpty()) csvWriter.writeToFile(updatedTasks, taskFile)
        else throw TaskEditFailedException()
    }

    override suspend fun deleteTask(id: UUID) {
        val currentTasks = csvReader.read(taskFile)
        validateTasksExist(currentTasks)

        val taskIndex = findTaskIndexById(currentTasks, id)
        validateTaskExists(taskIndex)

        val updatedTasks = removeTaskFromList(currentTasks, taskIndex)
        if (updatedTasks.isNotEmpty()) csvWriter.writeToFile(updatedTasks, taskFile)
        else throw TaskDeletionFailedException()
    }

    override suspend fun getAllTasks(): List<Task> {
        return csvReader.read(taskFile).ifEmpty {
            throw TaskFetchAllFailedException()
        }
    }

    override suspend fun getTask(id: UUID): Task {
        val tasks = csvReader.read(taskFile)
        return tasks.find { it.id == id } ?: throw TaskNotFoundException()
    }

    override suspend fun getTasksByStateAndProjectIds(projectId: UUID, stateId: UUID): List<Task> {
        val tasks = getAllTasks()

        val filteredTasks = tasks.filter { it.projectId == projectId && it.state.id == stateId }
        return filteredTasks.ifEmpty {
            throw TaskNotFoundException()
        }
    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        val tasks = csvReader.read(taskFile)
        val tasksForProject = tasks.filter { it.projectId == projectId }
        return tasksForProject.ifEmpty { throw TaskNotFoundException() }
    }


    private fun validateTasksExist(tasks: List<Task>) {
        if (tasks.isEmpty()) throw TaskNotFoundException()
    }

    private fun findTaskIndexById(tasks: List<Task>, id: UUID): Int {
        return tasks.indexOfFirst { it.id == id }
    }


    private fun validateTaskExists(taskIndex: Int) {
        if (taskIndex == -1) {
            throw TaskEditFailedException()
        }
    }

    private fun addTaskToList(tasks: List<Task>, newTask: Task): List<Task> {
        return tasks.toMutableList().apply {
            add(newTask)
        }
    }

    private fun updateTaskInList(tasks: List<Task>, index: Int, updatedTask: Task): List<Task> {
        return tasks.toMutableList().apply {
            this[index] = updatedTask
        }
    }

    private fun removeTaskFromList(tasks: List<Task>, index: Int): List<Task> {
        return tasks.toMutableList().apply {
            removeAt(index)
        }
    }


}