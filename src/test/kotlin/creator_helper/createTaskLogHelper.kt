package creator_helper

import org.example.data.model.Task
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


val testTaskId: UUID = UUID.randomUUID()

val taskLog = createTaskLogHelper(
    previousEntity = createTaskHelper(title = "Task 1"),
    currentEntity = createTaskHelper(title = "Task 2")
)

val taskLogsByTaskId = listOf(
    createTaskLogHelper(
        entityId = testTaskId,
        previousEntity = createTaskHelper(title = "Task 1"),
        currentEntity = createTaskHelper(title = "Task 2")
    ),
    createTaskLogHelper(
        entityId = testTaskId,
        previousEntity = createTaskHelper(title = "Task 2"),
        currentEntity = createTaskHelper(title = "Task 3")
    )
)

val taskLogsForTestUser =
    listOf(
        createTaskLogHelper(
            userId = testUserId,
            previousEntity = createTaskHelper(title = "Task 1"),
            currentEntity = createTaskHelper(title = "Task 2")
        ),
        createTaskLogHelper(
            userId = testUserId,
            previousEntity = createTaskHelper(title = "Task 1"),
            currentEntity = createTaskHelper(title = "Task 2")
        ),
    )

val taskLogsForAllUsers =
    listOf(
        createTaskLogHelper(
            userId = testUserId,
            previousEntity = createTaskHelper(title = "Task 1"),
            currentEntity = createTaskHelper(title = "Task 2")
        ),
        createTaskLogHelper(
            userId = testUserId,
            previousEntity = createTaskHelper(title = "Task 1"),
            currentEntity = createTaskHelper(title = "Task 2")
        ),
        createTaskLogHelper(
            previousEntity = createTaskHelper(title = "Task 1"),
            currentEntity = createTaskHelper(title = "Task 2")
        ),
    )