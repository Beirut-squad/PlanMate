package creator_helper

import domain.model.State
import java.util.*

fun createStateHelper(
    id: UUID = UUID.randomUUID(),
    name: String = "To Do"
): State {
    return State(
        id = id,
        name = name
    )
}