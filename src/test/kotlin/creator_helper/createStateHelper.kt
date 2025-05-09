package creator_helper

import data.csv.model.State
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