package di

import ui.common.exception.handler.SafeExecutor
import domain.useCase.authentication.*
import domain.useCase.log.CreateProjectLogUseCase
import domain.useCase.log.CreateTaskLogUseCase
import domain.useCase.log.GetAllProjectLogsUseCase
import domain.useCase.log.GetAllTaskLogsUseCase
import domain.useCase.project.*
import domain.useCase.state.CreateStateUseCase
import domain.useCase.state.DeleteStateUseCase
import domain.useCase.state.EditStateUseCase
import domain.useCase.task.*
import ui.common.exception.handler.ExceptionHandler
import domain.useCase.authentication.encryption.EncryptPassword
import domain.useCase.authentication.encryption.Encryptor
import domain.useCase.authentication.encryption.EncryptorMD5Impl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val logicModule = module {
    singleOf(::GetCurrentUserUseCase)
    singleOf(::LoginUseCase)
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
    singleOf(::RegisterUserUseCase)
    singleOf(::GetUserByIdUseCase)
    singleOf(::EncryptPassword)
    singleOf(::CreateProjectUseCase)
    singleOf(::GetAllProjectsUseCase)
    singleOf(::CreateProjectLogUseCase)
    singleOf(::DeleteProjectUseCase)
    singleOf(::EditProjectTitleUseCase)
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
    singleOf(::GetAllTaskLogsUseCase)
    singleOf(::ExceptionHandler)
    singleOf(::SafeExecutor)
}