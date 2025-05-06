package org.example.data.datasource.project_data_source

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.UUID

class ProjectMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : ProjectDataSource {
    override suspend fun createProject(project: Project) {
        withContext(Dispatchers.IO) {
            try {
                mongoConnection.projects.insertOne(project.toDocument())
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun editProject(project: Project) {
        withContext(Dispatchers.IO) {
            try {
                val updateResult = mongoConnection.projects.replaceOne(
                    eq(PROJECT_ID_FIELD, project.id.toString()),
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
                mongoConnection.projects.deleteOne(Filters.eq(PROJECT_ID_FIELD, id.toString()))
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

    override suspend fun getProject(id: UUID): Project = withContext(Dispatchers.IO) {
        try {
            val doc = mongoConnection.projects.find(eq(PROJECT_ID_FIELD, id.toString())).firstOrNull()
                ?: throw NoSuchElementException("No project found with ID: $id")
            doc.toProject()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun addStateToProject(
        projectId: UUID,
        state: State
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun editStateToProject(
        projectId: UUID,
        state: State
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeStateFromProject(
        projectId: UUID,
        state: State
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


    private fun Document.toStateList(): List<State> {
        return (this.get(STATE_FIELD) as? List<*>)?.mapNotNull { stateDoc ->
            (stateDoc as? Document)?.let {
                State(
                    id = UUID.fromString(it.getString(STATE_ID_FIELD)),
                    name = it.getString(NAME_FIELD)
                )
            }
        } ?: emptyList()
    }

    private fun Document.toProject(): Project {
        val states = this.toStateList()

        return Project(
            id = UUID.fromString(this.getString(PROJECT_ID_FIELD)),
            name = this.getString(NAME_FIELD),
            description = this.getString(DESCRIPTION_FIELD),
            creatorUserID = UUID.fromString(this.getString(CREATOR_USER_ID_FIELD)),
            createdAt = LocalDateTime.parse(this.getString(CREATED_AT_FIELD)),
            updatedAt = LocalDateTime.parse(this.getString(UPDATED_AT_FIELD)),
            state = states
        )
    }

    private fun Project.toDocument(): Document {
        return Document(PROJECT_ID_FIELD, id.toString())
            .append(NAME_FIELD, name)
            .append(DESCRIPTION_FIELD, description)
            .append(CREATOR_USER_ID_FIELD, creatorUserID.toString())
            .append(CREATED_AT_FIELD, createdAt.toString())
            .append(UPDATED_AT_FIELD, updatedAt.toString())
            .append(STATE_FIELD, state.map {
                Document(STATE_ID_FIELD, it.id.toString()).append(NAME_FIELD, it.name)
            })
    }

    companion object {
        private const val PROJECT_ID_FIELD = "_id"
        private const val STATE_ID_FIELD = "id"
        private const val NAME_FIELD = "name"
        private const val DESCRIPTION_FIELD = "description"
        private const val CREATOR_USER_ID_FIELD = "creatorUserID"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val STATE_FIELD = "state"
    }

}