package org.example.di

import logic.use_cases.log.CreateProjectLogUseCase
import logic.use_cases.log.CreateTaskLogUseCase
import logic.use_cases.log.GetUserProjectLogsUseCase
import logic.use_cases.project_manegment.EditProjectDescriptionUseCase
import logic.use_cases.project_manegment.EditProjectNameUseCase
import org.example.logic.use_cases.authentication.*

import org.example.logic.use_cases.log.GetAllProjectLogsUseCase
import org.example.logic.use_cases.project_manegment.GetProjectsForUserByIdUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskByStateIdAndProjectId
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.logic.use_cases.task_managemnt.CreateTaskUseCase

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import org.example.logic.use_cases.authentication.encryption.EncryptPassword
import org.example.logic.use_cases.authentication.encryption.Encryptor
import org.example.logic.use_cases.authentication.encryption.EncryptorMD5Impl
import org.example.logic.use_cases.project_manegment.DeleteProjectUseCase
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.logic.use_cases.task_managemnt.DeleteTaskUseCase
import org.example.logic.use_cases.task_managemnt.EditTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetAllTasksUseCase
import org.example.logic.use_cases.task_managemnt.GetTaskUseCase
import org.example.logic.use_cases.task_managemnt.GetTasksForProjectUseCase

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
    singleOf(::GetProjectsForUserByIdUseCase)
    singleOf(::GetTaskByStateIdAndProjectId)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::CreateTaskUseCase)
    singleOf(::CreateTaskLogUseCase)
    singleOf(::GetCurrentLoggedInUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::RegisterMateUseCase)
    singleOf(::RegisterUserOrAdminUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::EncryptPassword)
    singleOf(::CreateProjectUseCase)
    singleOf(::CreateProjectLogUseCase)
    singleOf(::DeleteProjectUseCase)
    singleOf(::EditProjectNameUseCase)
    singleOf(::EditProjectDescriptionUseCase)
    singleOf(::GetAllTasksUseCase)
    singleOf(::GetTasksForProjectUseCase)
    singleOf(::EditTaskUseCase)
    singleOf(::GetTaskUseCase)
    singleOf(::GetTaskLogsByTaskIdUseCase)
    singleOf(::DeleteTaskUseCase)
}