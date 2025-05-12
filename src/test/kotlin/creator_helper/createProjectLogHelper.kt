package creator_helper

import domain.model.Project
import domain.model.ProjectLog
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

val testUserId: UUID = UUID.fromString("10000000-0000-0000-0000-000000000000")
val project1 = createProjectHelper(name = "Project 1")
val project2 = createProjectHelper(name = "Project 2")
val project3 = createProjectHelper(name = "Project 3")

val projectLogsForAllUsers =
    listOf(
        createProjectLogHelper(
            userId = testUserId,
            entityId = listOfNotNull(project2.id, project1.id).first(),
            previousEntity = project1,
            currentEntity = project2
        ),
        createProjectLogHelper(
            userId = testUserId,
            entityId = listOfNotNull(project3.id, project2.id).first(),
            previousEntity = project2,
            currentEntity = project3
        ),
        createProjectLogHelper(
            entityId = listOfNotNull(project3.id, project2.id).first(),
            previousEntity = project2,
            currentEntity = project3
        )
    )

val projectLog = createProjectLogHelper(
    userId = testUserId,
    entityId = listOfNotNull(project2.id, project1.id).first(),
    previousEntity = project1,
    currentEntity = project2
)