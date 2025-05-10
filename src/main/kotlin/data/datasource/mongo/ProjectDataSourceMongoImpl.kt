package org.example.data.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.datasource.mapper.toDocument
import data.datasource.mapper.toProject
import domain.model.Project
import domain.model.State
import domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.data.datasource.mongo.mongo_db.MongoConnection
import java.util.*

class ProjectDataSourceMongoImpl(
    private val mongoConnection: MongoConnection
) : org.example.data.datasource.ProjectDataSource {
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
            ) ?: throw NoSuchElementException("No project found with ID: ${project.id}")

        }
    }

    override suspend fun deleteProject(id: UUID) {
        withContext(Dispatchers.IO) {
            mongoConnection.projects.deleteOne(Filters.eq(ID_FILED, id.toString()))

        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            val projectsDoc = mongoConnection.projects.find().toList()
            projectsDoc.map {
                it?.toProject() ?: throw Exception("No any projects ")
            }

        }
    }

    override suspend fun getProject(id: UUID): Project {
        return withContext(Dispatchers.IO) {
            val document = mongoConnection.projects.find(Filters.eq(ID_FILED, id.toString())).firstOrNull()
                ?: throw NoSuchElementException("No project found with ID: $id")
            document.toProject()

        }
    }

    override suspend fun addState(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {
            val stateDoc = Updates.push(STATE_FILED, state.toDocument())

            mongoConnection.projects.updateOne(Filters.eq(ID_FILED, projectId.toString()), stateDoc)

            val updatedDoc = mongoConnection.projects.find(Filters.eq(ID_FILED, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()
        }
    }

    override suspend fun editState(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {
            val project = mongoConnection.projects.find(Filters.eq(ID_FILED, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found")

            val states = (project[STATE_FILED] as? List<*>)?.mapNotNull {
                (it as? Document)
            }?.toMutableList() ?: mutableListOf()

            val index = states.indexOfFirst { it.getString(ID_FILED) == state.id.toString() }
            if (index == -1) throw NoSuchElementException("State not found in project")

            states[index] = state.toDocument()

            val update = Updates.set(STATE_FILED, states)
            mongoConnection.projects.updateOne(Filters.eq(ID_FILED, projectId.toString()), update)
            project.toProject()


        }
    }

    override suspend fun deleteState(projectId: UUID, state: State): Project {
        return withContext(Dispatchers.IO) {
            val filter = Filters.eq(ID_FILED, projectId.toString())
            val update = Updates.pull(STATE_FILED, Document(ID_FILED, state.id.toString()))
            val result = mongoConnection.projects.updateOne(filter, update)

            if (result.modifiedCount == 0L) {
                throw NoSuchElementException("State not found or already removed from project $projectId")
            }
            val updatedDoc = mongoConnection.projects.find(Filters.eq(ID_FILED, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()

        }
    }

    override suspend fun getMateProjectsByUserId(userId: UUID): List<Project> {
        return withContext(Dispatchers.IO) {
            val filter = Filters.elemMatch(
                USERS_FILED, Filters.and(
                    Filters.eq(ID_FILED, userId.toString()),
                )
            )

            val projectDocs = mongoConnection.projects.find(filter).toList()
            projectDocs.mapNotNull { it?.toProject() }
        }
    }

    override suspend fun addMate(projectId: UUID, user: User): Project {
        return withContext(Dispatchers.IO) {
            val update = Updates.push(USERS_FILED, user.toDocument())

            val result = mongoConnection.projects.updateOne(Filters.eq(ID_FILED, projectId.toString()), update)

            if (result.matchedCount == 0L) {
                throw NoSuchElementException("No project found with ID: $projectId")
            }

            val updatedDoc = mongoConnection.projects.find(Filters.eq(ID_FILED, projectId.toString())).firstOrNull()
                ?: throw NoSuchElementException("Project not found after update")

            updatedDoc.toProject()
        }
    }

    override suspend fun getUserProjectsById(userId: UUID): List<Project> {
        return withContext(Dispatchers.IO) {
            val filter = Filters.elemMatch(USERS_FILED, Filters.eq(ID_FILED, userId.toString()))
            val projectDocs = mongoConnection.projects.find(filter).toList()
            projectDocs.mapNotNull { it?.toProject() }
        }
    }

    companion object {
        private const val ID_FILED = "_id"
        private const val STATE_FILED = "state"
        private const val USERS_FILED = "users"
    }

}