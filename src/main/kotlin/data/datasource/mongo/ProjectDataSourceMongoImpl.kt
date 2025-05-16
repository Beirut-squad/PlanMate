package data.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.datasource.ProjectDataSource
import data.datasource.mongo.mapper.toDocument
import data.datasource.mongo.mapper.toProject
import ui.common.exception.ProjectNotFoundException
import ui.common.exception.StateNotFoundException
import domain.model.Project
import domain.model.TaskState
import domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import data.datasource.mongo.mongo_db_connection.MongoConnection
import java.util.*

class ProjectDataSourceMongoImpl(
    private val mongoConnection: MongoConnection
) : ProjectDataSource {
    override suspend fun createProject(project: Project) {
        withContext(Dispatchers.IO) {
            mongoConnection.projects.insertOne(project.toDocument())
        }
    }

    override suspend fun editProject(project: Project) {
        withContext(Dispatchers.IO) {
            mongoConnection.projects.replaceOne(
                Filters.eq(ID_FILED, project.id.toString()),
                project.toDocument()
            ) ?: throw ProjectNotFoundException()
        }
    }

    override suspend fun deleteProject(id: UUID) {
        withContext(Dispatchers.IO) {
            mongoConnection
                .projects.
                deleteOne(Filters.eq(ID_FILED, id.toString()))
        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            val projectsDoc = mongoConnection
                .projects
                .find()
                .toList()

            projectsDoc.map {
                it?.toProject() ?: throw ProjectNotFoundException()
            }

        }
    }

    override suspend fun getProject(id: UUID): Project {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection
                .projects
                .find(Filters.eq(ID_FILED, id.toString()))
                .firstOrNull()
                ?: throw ProjectNotFoundException()
            document.toProject()

        }
    }

    override suspend fun addState(projectId: UUID, taskState: TaskState): Project {
        return withContext(Dispatchers.IO) {
            val stateDoc = Updates.push(STATE_FILED, taskState.toDocument())

            mongoConnection
                .projects
                .updateOne(Filters.eq(ID_FILED, projectId.toString()), stateDoc)

            val updatedDoc = mongoConnection.projects.find(Filters.eq(ID_FILED, projectId.toString())).firstOrNull()
                ?: throw ProjectNotFoundException()

            updatedDoc.toProject()
        }
    }

    override suspend fun editState(projectId: UUID, taskState: TaskState): Project {
        return withContext(Dispatchers.IO) {
            val project = mongoConnection
                .projects
                .find(Filters.eq(ID_FILED, projectId.toString()))
                .firstOrNull()
                ?: throw ProjectNotFoundException()

            val states = (project[STATE_FILED] as? List<*>)?.mapNotNull {
                (it as? Document)
            }?.toMutableList() ?: mutableListOf()

            val index = states.indexOfFirst { it.getString(ID_FILED) == taskState.id.toString() }
            if (index == -1) throw StateNotFoundException()

            states[index] = taskState.toDocument()

            val update = Updates.set(STATE_FILED, states)
            mongoConnection.projects.updateOne(Filters.eq(ID_FILED, projectId.toString()), update)
            project.toProject()


        }
    }

    override suspend fun deleteState(projectId: UUID, taskState: TaskState): Project {
        return withContext(Dispatchers.IO) {
            val filter = Filters.eq(ID_FILED, projectId.toString())
            val update = Updates.pull(STATE_FILED, Document(ID_FILED, taskState.id.toString()))
            val result = mongoConnection.projects.updateOne(filter, update)

            if (result.modifiedCount == 0L) {
                throw StateNotFoundException()
            }
            val updatedDoc = mongoConnection
                .projects
                .find(Filters.eq(ID_FILED, projectId.toString()))
                .firstOrNull()
                ?: throw ProjectNotFoundException()

            updatedDoc.toProject()

        }
    }

    override suspend fun addMate(projectId: UUID, user: User): Project {
        return withContext(Dispatchers.IO) {
            val update = Updates.push(USERS_FILED, user.toDocument())

            val result = mongoConnection
                .projects
                .updateOne(Filters.eq(ID_FILED, projectId.toString()), update)

            if (result.matchedCount == 0L) {
                throw ProjectNotFoundException()
            }

            val updatedDoc = mongoConnection
                .projects
                .find(Filters.eq(ID_FILED, projectId.toString()))
                .firstOrNull()
                ?: throw ProjectNotFoundException()

            updatedDoc.toProject()
        }
    }

    override suspend fun getUserProjectsById(userId: UUID): List<Project> {
        return withContext(Dispatchers.IO) {
            val filter = Filters.elemMatch(USERS_FILED, Filters.eq(ID_FILED, userId.toString()))
            val projectDocs = mongoConnection
                .projects
                .find(filter)
                .toList()

            projectDocs.mapNotNull { it?.toProject() }
        }
    }

    companion object {
        private const val ID_FILED = "_id"
        private const val STATE_FILED = "state"
        private const val USERS_FILED = "users"
    }

}