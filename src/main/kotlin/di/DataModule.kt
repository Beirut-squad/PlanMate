package org.example.di

import org.example.data.csv.CsvReader
import org.example.data.csv.user_csv.UserCsvParser
import org.example.data.csv.user_csv.UserCsvWriter
import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.data.datasource.authentication_data_source.AuthenticationDataSourceImpl
import org.example.data.repositories.authentication_repository.AuthenticationRepositoryImpl
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthenticationDataSource> {
        AuthenticationDataSourceImpl(UserCsvWriter(), CsvReader(UserCsvParser()))
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }
}