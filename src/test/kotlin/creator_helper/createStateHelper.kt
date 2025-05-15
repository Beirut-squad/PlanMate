package creator_helper

import domain.model.TaskState
import java.util.*

fun createStateHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "To Do"
): TaskState {
    return TaskState(
        id = id,
        name = name
    )
}