package creator_helper

import org.example.models.Project
import org.example.models.ProjectLog
import org.example.models.Task
import org.example.models.TaskLog
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