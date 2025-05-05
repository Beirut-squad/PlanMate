package org.example.data.datasource.project_data_source

import data.mongo_db.MongoConnection
import org.bson.Document
import org.example.models.Project
import org.example.models.State
import java.util.UUID

class ProjectMongoDataSourceImpl(
   private val mongoConnection: MongoConnection
): ProjectDataSource {
    override suspend fun createProject(project: Project){
        val docProject = Document(ID_FIELD, project.id.toString())
            .append(NAME_FIELD, project.name)
            .append(DESCRIPTION_FIELD, project.description)
            .append(CREATOR_USER_ID_FIELD, project.creatorUserID.toString())
            .append(CREATED_AT_FIELD, project.createdAt)
            .append(UPDATED_AT_FIELD, project.updatedAt)
//            .append(STATE_FIELD, project.state)
        try {
            mongoConnection.projects.insertOne(docProject)
            print("Project created successfully")
        }catch (e: Exception){
            print("Error creating project: ${e.message}")
            throw e
        }


    }

    override fun editProject(project: Project): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(id: UUID): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
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

    companion object{
        private const val DATABASE_NAME = "Projects"
        private const val COLLECTION_NAME = "Projects"
        private const val ID_FIELD = "_id"
        private const val NAME_FIELD = "name"
        private const val DESCRIPTION_FIELD = "description"
        private const val CREATOR_USER_ID_FIELD = "creatorUserID"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val STATE_FIELD = "state"
    }

}