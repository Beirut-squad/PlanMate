package org.example.data.datasource.project_data_source

import com.mongodb.client.model.Filters
import data.mongo_db.MongoConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.UUID
import kotlin.compareTo

class ProjectMongoDataSourceImpl(
    private val mongoConnection: MongoConnection
) : ProjectDataSource {
    override suspend fun createProject(project: Project) {
        withContext(Dispatchers.IO) {
            try {
                val stateDocs = project.state.map { state ->
                    Document()
                        .append(ID_FIELD, state.id.toString())
                        .append(NAME_FIELD, state.name)
                }
                val projectDoc = Document(ID_FIELD, project.id.toString())
                    .append(NAME_FIELD, project.name)
                    .append(DESCRIPTION_FIELD, project.description)
                    .append(CREATOR_USER_ID_FIELD, project.creatorUserID.toString())
                    .append(CREATED_AT_FIELD, project.createdAt.toString())
                    .append(UPDATED_AT_FIELD, project.updatedAt.toString())
                    .append(STATE_FIELD, stateDocs)

                mongoConnection.projects.insertOne(projectDoc)
                println("Project ${project.name} created successfully")
            } catch (e: Exception) {
                println("Error creating project: ${e.message}")
            }
        }
    }

    override fun editProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(id: UUID) {
        withContext(Dispatchers.IO) {
            try {
                val result = mongoConnection.projects.deleteOne(Filters.eq("_id", id.toString()))
                if (result.deletedCount > 0) {
                    println("Project with ID $id deleted successfully")
                } else {
                    println("No project found with ID $id")
                }
            } catch (e: Exception) {
                println("Error deleting project: ${e.message}")
            }
        }
    }


    override suspend fun getAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            try {
                val projectsDoc = mongoConnection.projects.find().toList()
                if (projectsDoc.isEmpty()) {
                    emptyList()
                } else {
                    projectsDoc.map { projects ->
                        val states =(projects.get(STATE_FIELD) as? List<*>)?.mapNotNull {
                                stateDoc ->
                            (stateDoc as? Document)?.let {
                                State(
                                    id = UUID.fromString(it.getString("id")),
                                    name = it.getString("name")
                                )
                            }
                        }
                            ?: emptyList()
                        Project(
                            id = UUID.fromString(projects.getString(ID_FIELD)),
                            name = projects.getString(NAME_FIELD),
                            description = projects.getString(DESCRIPTION_FIELD),
                            creatorUserID = UUID.fromString(projects.getString(CREATOR_USER_ID_FIELD)),
                            createdAt = LocalDateTime.parse(projects.getString(CREATED_AT_FIELD)),
                            updatedAt = LocalDateTime.parse(projects.getString(UPDATED_AT_FIELD)),
                            state = states
                        )
                    }
                }
            } catch (e: Exception) {
                println("Error fetching projects: ${e.message}")
                emptyList()
            }
        }

    }
    override fun getProject(id: UUID): Result<Project> {
        TODO("Not yet implemented")
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

    companion object {
        private const val ID_FIELD = "_id"
        private const val NAME_FIELD = "name"
        private const val DESCRIPTION_FIELD = "description"
        private const val CREATOR_USER_ID_FIELD = "creatorUserID"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val STATE_FIELD = "state"
    }

}