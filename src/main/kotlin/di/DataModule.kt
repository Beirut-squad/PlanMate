package org.example.di


import data.datasource.authentication.AuthenticationDataSource
import data.datasource.log.LogDataSource
import data.datasource.log.LogMongoDataSourceImpl
import data.datasource.project.ProjectDataSource
import data.datasource.task.TaskDataSource
import data.exception.handler.DataExceptionHandler
import data.mongo_db.MongoConnection
import domain.exception.handler.DomainExceptionHandler
import domain.exception.handler.ExceptionHandler
import org.example.data.datasource.authentication_data_source.AuthenticationMongoDataSourceImpl
import org.example.data.datasource.project_data_source.ProjectMongoDataSourceImpl
import org.example.data.datasource.task_data_source.TaskMongoDataSourceImpl
import org.example.data.fake_datasource.AuthenticationFakeDataSource
import org.example.data.fake_datasource.LogFakeDataSource
import org.example.data.fake_datasource.ProjectFakeDataSource
import org.example.data.fake_datasource.TaskFakeDataSource
import org.example.data.repository.AuthenticationRepositoryImpl
import org.example.data.repository.LogRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.repository.TaskRepositoryImpl
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogRepository
import org.example.domain.repository.ProjectRepository
import org.example.domain.repository.TaskRepository
import org.example.ui.exception.UIExceptionHandler
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationMongoDataSourceImpl(MongoConnection, DomainExceptionHandler(get()))
    }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
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

    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }

    single<ExceptionHandler> {
        DataExceptionHandler(get())
    }
}