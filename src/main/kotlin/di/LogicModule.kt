package org.example.di

import core.exception.handler.DefaultExceptionHandler
import domain.exception.handler.DomainExceptionHandler
import domain.exception.handler.ExceptionHandler
import domain.use_case.authentication.*
import domain.use_case.log.CreateProjectLogUseCase
import domain.use_case.log.CreateTaskLogUseCase
import domain.use_case.log.GetAllProjectLogsUseCase
import domain.use_case.project.*
import domain.use_case.state.CreateStateUseCase
import domain.use_case.state.DeleteStateUseCase
import domain.use_case.state.EditStateUseCase
import domain.use_case.task.*
import org.example.domain.use_cases.authentication.encryption.EncryptPassword
import org.example.domain.use_cases.authentication.encryption.Encryptor
import org.example.domain.use_cases.authentication.encryption.EncryptorMD5Impl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logicModule = module {
    singleOf(::GetCurrentUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::RegisterMateUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::EncryptPassword)
    single<Encryptor> {
        EncryptorMD5Impl()
    }
    singleOf(::GetAllProjectsUseCase)
    singleOf(::GetAllProjectLogsUseCase)
    singleOf(::GetUserProjectsByIdUseCase)
    singleOf(::GetTaskByStateIdAndProjectId)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::CreateTaskUseCase)
    singleOf(::CreateTaskLogUseCase)
    singleOf(::GetCurrentUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::RegisterMateUseCase)
    singleOf(::RegisterUserUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::EncryptPassword)
    singleOf(::CreateProjectUseCase)
    singleOf(::GetAllProjectsUseCase)
    singleOf(::CreateProjectLogUseCase)
    singleOf(::DeleteProjectUseCase)
    singleOf(::EditProjectNameUseCase)
    singleOf(::EditProjectDescriptionUseCase)
    singleOf(::DeleteStateUseCase)
    singleOf(::DeleteProjectStateUseCase)
    singleOf(::CreateStateUseCase)
    singleOf(::AddProjectStateUseCase)
    singleOf(::EditProjectStateUseCase)
    singleOf(::EditStateUseCase)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::AddProjectMateUseCase)
    singleOf(::GetAllUsersUseCase)
    singleOf(::GetProjectTasksUseCase)
    singleOf(::EditTaskUseCase)
    singleOf(::DeleteTaskUseCase)

    single<ExceptionHandler> {
        DomainExceptionHandler(get())
    }

    single<ExceptionHandler> {
        DefaultExceptionHandler(get())
    }

}