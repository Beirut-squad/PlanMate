package data.datasource.mongo.mapper

import domain.model.*
import org.bson.Document
import java.time.LocalDateTime
import java.util.UUID

fun User.toDocument(): Document {
    return Document(ID_FILED, this.id.toString()).append(NAME_FILED, this.name).append(EMAIL_FILED, this.email)
        .append(PASSWORD_FILED, this.password).append(ROLE_FILED, this.role.name)
        .append(IS_DELETED_FILED, this.isDeleted)
}

fun Document.toUser(): User {
    return User(
        id = UUID.fromString(this.getString(ID_FILED)),
        name = this.getString(NAME_FILED),
        password = this.getString(PASSWORD_FILED),
        email = this.getString(EMAIL_FILED),
        role = Role.valueOf(this.getString(ROLE_FILED)),
        isDeleted = this.getBoolean(IS_DELETED_FILED, false)
    )
}

fun ProjectLog.toDocument(): Document =
    Document().append(ID_FILED, id.toString()).append(USER_ID_FILED, userId.toString())
        .append(ENTITY_ID_FILED, entityId.toString()).append(PREVIOUS_ENTITY_FILED, previousEntity?.toDocument())
        .append(CURRENT_ENTITY_FILED, currentEntity?.toDocument()).append(CREATED_AT_FILED, createdAt.toString())

fun TaskLog.toDocument(): Document = Document().append(ID_FILED, id.toString()).append(USER_ID_FILED, userId.toString())
    .append(ENTITY_ID_FILED, entityId.toString()).append(PREVIOUS_ENTITY_FILED, previousEntity?.toDocument())
    .append(CURRENT_ENTITY_FILED, currentEntity?.toDocument()).append(CREATED_AT_FILED, createdAt.toString())

fun Document.toProjectLog(): ProjectLog = ProjectLog(
    id = UUID.fromString(getString(ID_FILED)),
    userId = UUID.fromString(getString(USER_ID_FILED)),
    entityId = UUID.fromString(getString(ENTITY_ID_FILED)),
    previousEntity = get(PREVIOUS_ENTITY_FILED, Document::class.java)?.toProject(),
    currentEntity = get(CURRENT_ENTITY_FILED, Document::class.java)?.toProject(),
    createdAt = LocalDateTime.parse(getString(CREATED_AT_FILED))
)

fun Document.toTaskLog(): TaskLog = TaskLog(
    id = UUID.fromString(getString(ID_FILED)),
    userId = UUID.fromString(getString(USER_ID_FILED)),
    entityId = UUID.fromString(getString(ENTITY_ID_FILED)),
    previousEntity = get(PREVIOUS_ENTITY_FILED, Document::class.java)?.toTask(),
    currentEntity = get(CURRENT_ENTITY_FILED, Document::class.java)?.toTask(),
    createdAt = LocalDateTime.parse(getString(CREATED_AT_FILED))
)

fun Task.toDocument(): Document {
    return Document(ID_FILED, id.toString()).append(PROJECT_ID_FILED, projectId.toString()).append(TITLE_FILED, title)
        .append(DESCRIPTION_FILED, description).append(STATE_FILED, state.toDocument())
        .append(CREATOR_USER_ID_FILED, creatorUserID.toString()).append(CREATED_AT_FILED, createdAt.toString())
        .append(UPDATED_AT_FILED, updatedAt.toString())
}

fun Document.toTask(): Task {
    return Task(
        id = UUID.fromString(this.getString(ID_FILED)),
        projectId = UUID.fromString(this.getString(PROJECT_ID_FILED)),
        title = this.getString(TITLE_FILED),
        description = this.getString(DESCRIPTION_FILED),
        state = this.toState(),
        creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FILED)),
        createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FILED)),
        updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FILED))
    )
}
fun Document.toState(): State {
    val state = this[STATE_FILED] as Document
    return State(
        id = UUID.fromString(state.getString(ID_FILED)),
        name = state.getString(NAME_FILED)
    )
}
fun Document.toProject(): Project {
    val states = this.toStateList()
    val users = this.toUserList(USERS_FILED)

    return Project(
        id = UUID.fromString(this.getString(ID_FILED)),
        title = this.getString(NAME_FILED),
        description = this.getString(DESCRIPTION_FILED),
        creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FILED)),
        createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FILED)),
        updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FILED)),
        states = states,
        users = users
    )
}

fun Project.toDocument(): Document {
    return Document(ID_FILED, id.toString()).append(NAME_FILED, title).append(DESCRIPTION_FILED, description)
        .append(CREATOR_USER_ID_FILED, creatorUserID.toString()).append(CREATED_AT_FILED, createdAt.toString())
        .append(UPDATED_AT_FILED, updatedAt.toString()).append(STATE_FILED, states.map {
            Document(ID_FILED, it.id.toString()).append(NAME_FILED, it.name)
        }).append(USERS_FILED, users.map {
            Document(ID_FILED, it.id.toString()).append(NAME_FILED, it.name).append(PASSWORD_FILED, it.password)
                .append(EMAIL_FILED, it.email).append(ROLE_FILED, it.role.name).append(IS_DELETED_FILED, it.isDeleted)
        })
}

fun State.toDocument(): Document {
    return Document().append(ID_FILED, this.id.toString()).append(NAME_FILED, this.name)
}

fun Document.toUserList(filedName: String = USERS_FILED): List<User> {
    return (this[filedName] as? List<*>)?.mapNotNull { userDoc ->
        (userDoc as? Document)?.let {
            User(
                id = UUID.fromString(it.getString(ID_FILED)),
                name = it.getString(NAME_FILED),
                password = it.getString(PASSWORD_FILED),
                email = it.getString(EMAIL_FILED),
                role = Role.valueOf(it.getString(ROLE_FILED)),
                isDeleted = it.getBoolean(IS_DELETED_FILED)
            )
        }
    } ?: emptyList()
}

private fun Document.toStateList(): List<State> {
    return (this[STATE_FILED] as? List<*>)?.mapNotNull { stateDoc ->
        (stateDoc as? Document)?.let {
            State(
                id = UUID.fromString(it.getString(ID_FILED)), name = it.getString(NAME_FILED)
            )
        }
    } ?: emptyList()
}

private const val USERS_FILED = "users"
private const val ROLE_FILED = "role"
private const val DESCRIPTION_FILED = "description"
private const val CREATOR_USER_ID_FILED = "creatorUserID"
private const val UPDATED_AT_FILED = "updatedAt"
private const val STATE_FILED = "state"
private const val USER_ID_FILED = "userId"
private const val ENTITY_ID_FILED = "entityId"
private const val PREVIOUS_ENTITY_FILED = "previousEntity"
private const val CURRENT_ENTITY_FILED = "currentEntity"
private const val CREATED_AT_FILED = "createdAt"
private const val PROJECT_ID_FILED = "projectId"
private const val TITLE_FILED = "title"
private const val ID_FILED = "_id"
private const val NAME_FILED = "name"
private const val EMAIL_FILED = "email"
private const val PASSWORD_FILED = "password"
private const val IS_DELETED_FILED = "isDeleted"

