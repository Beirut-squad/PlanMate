package creator_helper

import org.example.models.State
import java.util.*

fun createTestTaskState(
    id: UUID = UUID.randomUUID(),
    name: String = "To Do"
): State {
    return State(
        id = id,
        name = name
    )
}