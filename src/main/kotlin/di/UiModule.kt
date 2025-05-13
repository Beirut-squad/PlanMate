package di

import domain.use_case.project.GetAllProjectsUseCase
import domain.use_case.project.GetProjectByIdUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.components.Colors
import ui.components.Printer
import ui.components.Reader
import ui.components.Validator
import ui.view.authentication.LoginUi
import ui.view.authentication.RegisterUi
import ui.view.authentication.StartUpMenuUi
import ui.view.project.ProjectMateUi
import ui.view.project.ProjectStateSelectedUi
import ui.view.project.ProjectTasksUi
import ui.view.project.ViewProjectsUi
import ui.view.task.CreateTaskUi
import ui.view.task.EditTaskUi
import ui.view.user.admin.home.AdminUi
import ui.view.user.admin.log.project.ProjectLogUi
import ui.view.user.admin.log.project.ProjectLogsUi
import ui.view.user.admin.log.task.TaskLogUi
import ui.view.user.admin.log.task.TaskLogsUi
import ui.view.user.admin.project.*
import ui.view.user.mate.MateUi
import ui.view.user.mate.UserProjectsUi

val uiModule = module {
    singleOf(::StartUpMenuUi)
    singleOf(::LoginUi)
    singleOf(::RegisterUi)
    singleOf(::Colors)
    singleOf(::Printer)
    singleOf(::Reader)
    singleOf(::AdminUi)
    singleOf(::MateUi)
    singleOf(::CreateProjectUi)
    singleOf(::GetAllProjectsUseCase)
    singleOf(::UserProjectsUi)
    singleOf(::ProjectMateUi)
    singleOf(::ProjectStateSelectedUi)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::CreateTaskUi)
    singleOf(::ProjectLogsUi)
    singleOf(::SingleProjectUi)
    singleOf(::EditProjectUi)
    singleOf(::EditTaskUi)
    singleOf(::ProjectTasksUi)
    singleOf(::ViewProjectsUi)
    singleOf(::EditProjectStateUi)
    singleOf(::DeleteProjectStateUi)
    singleOf(::ProjectStatesUi)
    singleOf(::SingleStateUi)
    singleOf(::AddProjectUserUi)
    singleOf(::ProjectLogUi)
    singleOf(::ProjectsUi)
    singleOf(::TaskLogsUi)
    singleOf(::TaskLogUi)
    singleOf(::Validator)
}