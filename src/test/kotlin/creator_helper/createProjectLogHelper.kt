package creator_helper

import org.example.models.Project
import org.example.models.ProjectLog
import java.time.LocalDateTime
import java.util.*

fun createProjectLogHelper(
    id: UUID = UUID.randomUUID(),
    userId: UUID = UUID.randomUUID(),
    entityId: UUID = UUID.randomUUID(),
    previousEntity: Project? = null,
    currentEntity: Project? = null,
    createdAt: LocalDateTime = LocalDateTime.now()
): ProjectLog {
    return ProjectLog(
        id = id,
        userId = userId,
        entityId = entityId,
        previousEntity = previousEntity,
        currentEntity = currentEntity,
        createdAt = createdAt,
    )
}

val testUserId: UUID = UUID.randomUUID()

val projectLogsForTestUser =
    listOf(
        createProjectLogHelper(
            userId = testUserId,
            previousEntity = createProjectHelper(name = "Project 1"),
            currentEntity = createProjectHelper(name = "Project 2")
        ),
        createProjectLogHelper(
            userId = testUserId,
            previousEntity = createProjectHelper(name = "Project 2"),
            currentEntity = createProjectHelper(name = "Project 3")
        )
    )

val projectLogsForAllUsers =
    listOf(
        createProjectLogHelper(
            userId = testUserId,
            previousEntity = createProjectHelper(name = "Project 1"),
            currentEntity = createProjectHelper(name = "Project 2")
        ),
        createProjectLogHelper(
            userId = testUserId,
            previousEntity = createProjectHelper(name = "Project 2"),
            currentEntity = createProjectHelper(name = "Project 3")
        ),
        createProjectLogHelper(
            previousEntity = createProjectHelper(name = "Project 2"),
            currentEntity = createProjectHelper(name = "Project 3")
        )
    )