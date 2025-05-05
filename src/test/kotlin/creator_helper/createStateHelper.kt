package creator_helper

import org.example.models.Project
import org.example.models.State
import java.util.*

fun createStateHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "To Do",
    projectId: UUID = UUID.randomUUID()
): State {
    return State(
        id = id,
        name = name,
        projectId = projectId
    )
}