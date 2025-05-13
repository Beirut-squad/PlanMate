package creator_helper

import domain.model.Task
import domain.model.TaskLog
import java.time.LocalDateTime
import java.util.*

fun createTaskLogHelper(
    id: UUID = UUID.randomUUID(),
    userId: UUID = UUID.randomUUID(),
    entityId: UUID = UUID.randomUUID(),
    previousEntity: Task? = null,
    currentEntity: Task? = null,
    createdAt: LocalDateTime = LocalDateTime.now()
): TaskLog {
    return TaskLog(
        id = id,
        userId = userId,
        entityId = entityId,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = createdAt,
    )
}

val project1Id: UUID = UUID.randomUUID()
val task1 = createTaskHelper(title = "Task 1", projectId = project1Id)
val task2 = createTaskHelper(title = "Task 2", projectId = project1Id)
val task3 = createTaskHelper(title = "Task 3", projectId = project1Id)
val task4 = createTaskHelper(title = "Task 4", projectId = project1Id)

val taskLog = createTaskLogHelper(
    entityId = listOfNotNull(task2.id, task1.id).first(), previousEntity = task1, currentEntity = task2
)

val taskLog23 = createTaskLogHelper(
    entityId = listOfNotNull(task3.id, task2.id).first(), previousEntity = task2, currentEntity = task3
)
val taskLog34 = createTaskLogHelper(
    entityId = listOfNotNull(task4.id, task3.id).first(), previousEntity = task3, currentEntity = task4
)

val taskLogsForAllUsers = listOf(
    taskLog23,
    taskLog23,
    taskLog34,
)