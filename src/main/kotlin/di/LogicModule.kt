package org.example.di

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import logic.use_cases.log.GetUserProjectLogsUseCase
import org.example.logic.use_cases.authentication.*
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.logic.use_cases.authentication.encryption.Encryptor
import org.example.logic.use_cases.authentication.encryption.EncryptorMD5Impl
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logicModule = module {
    singleOf(::GetCurrentLoggedInUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::RegisterMateUseCase)
    singleOf(::RegisterUserOrAdminUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::EncryptPassword)
    single<Encryptor> {
        EncryptorMD5Impl()
    }
    singleOf(::GetUserProjectLogsUseCase)
    singleOf(::GetAllProjectsUseCases)
    singleOf(::GetAllProjectLogsUseCase)
    singleOf(::CreateProjectUseCase)
    singleOf(::GetAllProjectsUseCases)
    singleOf(::CreateProjectLogUseCase)
}