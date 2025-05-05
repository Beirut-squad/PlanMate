package org.example.di

import data.csv.log_for_project_csv.LogCsvParserForProject
import data.csv.log_for_project_csv.LogCsvWriterForProject
import data.csv.project_csv.ProjectCsvParser
import data.csv.project_csv.ProjectCsvWriter
import data.csv.task_csv.TaskCsvParser
import data.mongo_db.MongoConnection
import org.example.data.csv.CsvReader
import org.example.data.csv.log_for_task_csv.LogCsvParserForTask
import org.example.data.csv.log_for_task_csv.LogCsvWriterForTask
import org.example.data.csv.state_csv.StateCsvParser
import org.example.data.csv.user_csv.UserCsvParser
import org.example.data.csv.user_csv.UserCsvWriter
import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.data.datasource.authentication_data_source.AuthenticationDataSourceImpl
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.log_data_source.LogDataSourceImpl
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.datasource.project_data_source.ProjectDataSourceImpl
import org.example.data.datasource.project_data_source.ProjectMongoDataSourceImpl
import org.example.data.repositories.authentication_repository.AuthenticationRepositoryImpl
import org.example.data.repositories.log_repository.LogRepositoryImpl
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.repositories.project_repository.ProjectRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationDataSourceImpl(UserCsvWriter(), CsvReader(UserCsvParser()))
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }

    single<LogDataSource> {
        LogDataSourceImpl(
            csvProjectLogReader = CsvReader(LogCsvParserForProject(ProjectCsvParser(StateCsvParser()))),
            csvProjectLogWriter = LogCsvWriterForProject(),
            csvTaskLogReader = CsvReader(LogCsvParserForTask(TaskCsvParser(StateCsvParser()))),
            csvTaskLogWriter = LogCsvWriterForTask()
        )
    }

    single<ProjectDataSource> {
        ProjectMongoDataSourceImpl(
            MongoConnection
        )
    }

    single<LogRepository> {
        LogRepositoryImpl(get())
    }

    single<ProjectRepository> {
        ProjectRepositoryImpl(get())
    }
}