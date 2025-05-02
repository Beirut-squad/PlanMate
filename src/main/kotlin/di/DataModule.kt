package org.example.di

import org.example.data.datasource.authentication_data_source.AuthenticationDataSource
import org.example.data.datasource.authentication_data_source.AuthenticationDataSourceImpl
import org.example.data.repositories.authentication_repository.AuthenticationRepositoryImpl
import org.example.logic.repositories.authentication_repository.AuthenticationRepository
import org.koin.dsl.module

val dataModule = module {
    // TODO: Add csv parser, reader and writer

    single<AuthenticationDataSource> {
        AuthenticationDataSourceImpl(get(), get())
    }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(get())
    }
}