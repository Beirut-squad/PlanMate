package org.example.di

import EditStateUseCase
import logic.use_cases.log.CreateProjectLogUseCase
import logic.use_cases.log.GetUserProjectLogsUseCase
import logic.use_cases.project_manegment.EditProjectDescriptionUseCase
import logic.use_cases.project_manegment.EditProjectNameUseCase
import org.example.logic.use_cases.authentication.*
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.logic.use_cases.authentication.encryption.Encryptor
import org.example.logic.use_cases.authentication.encryption.EncryptorMD5Impl
import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.logic.use_cases.project_manegment.*
import org.example.logic.use_cases.state_usecase.CreateStateUseCase
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
    singleOf(::DeleteProjectUseCase)
    singleOf(::EditProjectNameUseCase)
    singleOf(::EditProjectDescriptionUseCase)
    singleOf(::DeleteStateUseCase)
    singleOf(::RemoveStateFromProjectUseCase)
    singleOf(::CreateStateUseCase)
    singleOf(::AddStateToProjectUseCase)
    singleOf(::EditStateToProjectUseCase)
    singleOf(::EditStateUseCase)
}