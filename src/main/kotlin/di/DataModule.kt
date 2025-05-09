package org.example.di


import data.datasource.authentication.AuthenticationDataSource
import data.datasource.log.LogDataSource
import data.datasource.project.ProjectDataSource
import data.datasource.task.TaskDataSource
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
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationFakeDataSource()
    }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }

    single<LogDataSource> {
        LogFakeDataSource()
    }

    single<ProjectDataSource> {
        ProjectFakeDataSource()
    }

    single<TaskDataSource> {
        TaskFakeDataSource()
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