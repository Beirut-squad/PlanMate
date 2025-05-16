package di

import data.datasource.AuthenticationDataSource
import data.datasource.mongo.AuthenticationMongoDataSourceImpl
import data.datasource.mongo.LogDataSourceMongoImpl
import data.datasource.LogDataSource
import data.datasource.ProjectDataSource
import data.datasource.TaskDataSource
import data.datasource.mongo.ProjectDataSourceMongoImpl
import data.datasource.mongo.TaskMongoDataSourceImpl
import data.repository.AuthenticationRepositoryImpl
import data.datasource.mongo.mongo_db_connection.MongoConnection
import data.repository.LogRepositoryImpl
import data.repository.ProjectRepositoryImpl
import data.repository.TaskRepositoryImpl
import domain.repository.AuthenticationRepository
import domain.repository.LogRepository
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationMongoDataSourceImpl(MongoConnection)
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
}