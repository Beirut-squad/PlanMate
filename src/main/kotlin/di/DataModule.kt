package org.example.di


import data.mongo_db.MongoConnection
import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.data.datasource.authentication_data_source.AuthenticationMongoDataSourceImpl
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.log_data_source.LogMongoDataSourceImpl
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.datasource.project_data_source.ProjectMongoDataSourceImpl
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.data.datasource.task_data_source.TaskMongoDataSourceImpl
import org.example.data.repositories.authentication_repository.AuthenticationRepositoryImpl
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.data.repositories.task_repository.TaskRepositoryImpl
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.repositories.task_repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationMongoDataSourceImpl(MongoConnection)
    }

    single<LogDataSource> {
        LogMongoDataSourceImpl(MongoConnection)
    }

    single<ProjectDataSource> {
        ProjectMongoDataSourceImpl(MongoConnection)
    }

    single<TaskDataSource> {
        TaskMongoDataSourceImpl(MongoConnection)
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }

    single<LogRepository> {
        LogRepositoryImpl(get())
    }

    single<ProjectRepository> {
        ProjectRepositoryImpl(get())
    }

    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }
}