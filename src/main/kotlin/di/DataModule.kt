package org.example.di


import data.datasource.authentication.AuthenticationDataSource
import data.datasource.mongo.AuthenticationMongoDataSourceImpl
import data.datasource.mongo.LogDataSourceMongoImpl
import data.exception.handler.DataExceptionHandler
import domain.exception.handler.DomainExceptionHandler
import domain.exception.handler.ExceptionHandler
import org.example.data.datasource.LogDataSource
import org.example.data.datasource.ProjectDataSource
import org.example.data.datasource.TaskDataSource
import org.example.data.datasource.mongo.ProjectDataSourceMongoImpl
import data.datasource.mongo.TaskMongoDataSourceImpl
import data.repository.AuthenticationRepositoryImpl
import org.example.data.datasource.mongo.mongo_db.MongoConnection
import org.example.data.repository.LogRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.repository.TaskRepositoryImpl
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogRepository
import org.example.domain.repository.ProjectRepository
import org.example.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationMongoDataSourceImpl(MongoConnection, DomainExceptionHandler(get()))
    }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }

    single<LogDataSource> {
        LogDataSourceMongoImpl(MongoConnection)
    }

    single<ProjectDataSource> {
        ProjectDataSourceMongoImpl(MongoConnection)
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