package org.example.data.datasource.project_data_source

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.models.Project
import org.example.models.Role
import org.example.models.State
import org.example.models.User
import java.time.LocalDateTime
import java.util.*

class ProjectMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : ProjectDataSource {
    override suspend fun createProject(project: Project) {
        withContext(Dispatchers.IO) {
            mongoConnection.projects.insertOne(project.toDocument())

        }
    }

    override suspend fun editProject(project: Project) {
        withContext(Dispatchers.IO) {
            try {
                val updateResult = mongoConnection.projects.replaceOne(
                    eq(ID_FIELD, project.id.toString()),
                    project.toDocument()
                )
                if (updateResult.matchedCount == 0L) {
                    throw NoSuchElementException("No project found with ID: ${project.id}")
                }
            } catch (e: NoSuchElementException) {
                throw e
            }
        }
    }

    override suspend fun deleteProject(id: UUID) {
        withContext(Dispatchers.IO) {
            try {
                mongoConnection.projects.deleteOne(eq(ID_FIELD, id.toString()))
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            try {
                val projectsDoc = mongoConnection.projects.find().toList()
                projectsDoc.map {
                    it?.toProject() ?: throw Exception("No any projects ")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getProject(id: UUID): Project {
        return withContext(Dispatchers.IO) {
            try {
                val doc = mongoConnection.projects.find(eq(ID_FIELD, id.toString())).firstOrNull()
                    ?: throw NoSuchElementException("No project found with ID: $id")
                doc.toProject()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun addStateToProject(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {

            val update = Updates.push(STATE_FIELD, state.toDocument())
            val result = mongoConnection.projects.updateOne(eq(ID_FIELD, projectId.toString()), update)

            if (result.matchedCount == 0L) {
                throw NoSuchElementException("No project found with ID: $projectId")
            }
            val updatedDoc = mongoConnection.projects.find(eq(ID_FIELD, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()
        }
    }


    override suspend fun editStateToProject(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {
            val project = mongoConnection.projects.find(eq(ID_FIELD, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found")

            val states = (project[STATE_FIELD] as? List<*>)?.mapNotNull {
                (it as? Document)
            }?.toMutableList() ?: mutableListOf()

            val index = states.indexOfFirst { it.getString(ID_FIELD) == state.id.toString() }
            if (index == -1) throw NoSuchElementException("State not found in project")

            states[index] = state.toDocument()

            val update = Updates.set(STATE_FIELD, states)
            mongoConnection.projects.updateOne(eq(ID_FIELD, projectId.toString()), update)
            project.toProject()


        }
    }


    override suspend fun removeStateFromProject(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {
            val filter = eq(ID_FIELD, projectId.toString())
            val update = Updates.pull(STATE_FIELD, Document(ID_FIELD, state.id.toString()))
            val result = mongoConnection.projects.updateOne(filter, update)

            if (result.modifiedCount == 0L) {
                throw NoSuchElementException("State not found or already removed from project $projectId")
            }
            val updatedDoc = mongoConnection.projects.find(eq(ID_FIELD, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()

        }
    }

    override suspend fun getProjectForMateByUserId(userId: UUID): List<Project> {
        return withContext(Dispatchers.IO) {
            val filter = Filters.elemMatch(
                USERS_FIELD, Filters.and(
                    eq(ID_FIELD, userId.toString()),
                    eq(ROLE_FIELD, Role.MATE.name)
                )
            )

            val projectDocs = mongoConnection.projects.find(filter).toList()
            projectDocs.mapNotNull { it?.toProject() }
        }
    }


    override suspend fun addMateToProject(projectId: UUID, user: User): Project {
        return withContext(Dispatchers.IO) {
            val update = Updates.push(
                USERS_FIELD, Document(ID_FIELD, user.id.toString())
                    .append(NAME_FIELD, user.name)
                    .append(PASSWORD_FIELD, user.password)
                    .append(EMAIL_FIELD, user.email)
                    .append(ROLE_FIELD, user.role.name)
                    .append(IS_DELETED_FIELD, user.isDeleted)
            )

            val result = mongoConnection.projects.updateOne(eq(ID_FIELD, projectId.toString()), update)

            if (result.matchedCount == 0L) {
                throw NoSuchElementException("No project found with ID: $projectId")
            }

            val updatedDoc = mongoConnection.projects.find(eq(ID_FIELD, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()
        }
    }

    override suspend fun getProjectsForUserById(userId: UUID): List<Project> {
        return withContext(Dispatchers.IO) {
            val filter = Filters.elemMatch(USERS_FIELD, eq(ID_FIELD, userId.toString()))
            val projectDocs = mongoConnection.projects.find(filter).toList()
            projectDocs.mapNotNull { it?.toProject() }
        }
    }


    private fun Document.toStateList(): List<State> {
        return (this[STATE_FIELD] as? List<*>)?.mapNotNull { stateDoc ->
            (stateDoc as? Document)?.let {
                State(
                    id = UUID.fromString(it.getString(ID_FIELD)),
                    name = it.getString(NAME_FIELD)
                )
            }
        } ?: emptyList()
    }

    private fun Document.toProject(): Project {
        val states = this.toStateList()
        val users = this.toUserListFromField("users")

        return Project(
            id = UUID.fromString(this.getString(ID_FIELD)),
            name = this.getString(NAME_FIELD),
            description = this.getString(DESCRIPTION_FIELD),
            creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FIELD)),
            createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FIELD)),
            updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FIELD)),
            state = states,
            users = users
        )
    }

    private fun Project.toDocument(): Document {
        return Document(ID_FIELD, id.toString())
            .append(NAME_FIELD, name)
            .append(DESCRIPTION_FIELD, description)
            .append(CREATOR_USER_ID_FIELD, creatorUserID.toString())
            .append(CREATED_AT_FIELD, createdAt.toString())
            .append(UPDATED_AT_FIELD, updatedAt.toString())
            .append(STATE_FIELD, state.map {
                Document(ID_FIELD, it.id.toString()).append(NAME_FIELD, it.name)
            })
            .append(USERS_FIELD, users.map {
                Document(ID_FIELD, it.id.toString())
                    .append(NAME_FIELD, it.name)
                    .append(PASSWORD_FIELD, it.password)
                    .append(EMAIL_FIELD, it.email)
                    .append(ROLE_FIELD, it.role.name)
                    .append(IS_DELETED_FIELD, it.isDeleted)
            })
    }

    private fun State.toDocument(): Document {
        return Document()
            .append(ID_FIELD, this.id.toString())
            .append(NAME_FIELD, this.name)
    }

    private fun Document.toUserListFromField(fieldName: String = USERS_FIELD): List<User> {
        return (this[fieldName] as? List<*>)?.mapNotNull { userDoc ->
            (userDoc as? Document)?.let {
                User(
                    id = UUID.fromString(it.getString(ID_FIELD)),
                    name = it.getString(NAME_FIELD),
                    password = it.getString(PASSWORD_FIELD),
                    email = it.getString(EMAIL_FIELD),
                    role = Role.valueOf(it.getString(ROLE_FIELD)),
                    isDeleted = it.getBoolean(IS_DELETED_FIELD)
                )
            }
        } ?: emptyList()
    }

    companion object {
        private const val ID_FIELD = "_id"
        private const val NAME_FIELD = "name"
        private const val DESCRIPTION_FIELD = "description"
        private const val CREATOR_USER_ID_FIELD = "creatorUserID"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val STATE_FIELD = "state"
        private const val USERS_FIELD = "users"
        private const val ROLE_FIELD = "role"
        private const val IS_DELETED_FIELD = "isDeleted"
        private const val PASSWORD_FIELD = "<password"
        private const val EMAIL_FIELD = "email"

    }

}