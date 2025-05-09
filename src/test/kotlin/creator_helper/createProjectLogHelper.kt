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

val testUserId: UUID = UUID.fromString("10000000-0000-0000-0000-000000000000")
val project1 = createProjectHelper(name = "Project 1")
val project2 = createProjectHelper(name = "Project 2")
val project3 = createProjectHelper(name = "Project 3")
val project4 = createProjectHelper(name = "Project 4")

val projectLogsForTestUser =
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
        )
    )

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

val projectLogsByProjectId = listOf(
    createProjectLogHelper(
        entityId = listOfNotNull(project2.id, project1.id).first(),
        previousEntity = project1,
        currentEntity = project2
    ),
    createProjectLogHelper(
        entityId = listOfNotNull(project3.id, project2.id).first(),
        previousEntity = project2,
        currentEntity = project3
    )
)

val testProjectLogs = listOf(
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
        userId = testUserId,
        entityId = listOfNotNull(project4.id, project3.id).first(),
        previousEntity = project3,
        currentEntity = project4
    )
)




