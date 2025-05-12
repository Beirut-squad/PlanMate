package org.example.di

import domain.use_case.project.GetAllProjectsUseCase
import domain.use_case.project.GetProjectByIdUseCase
import ui.admin.home.AdminUi
import org.example.ui.admin.log.project.ProjectLogUi
import org.example.ui.admin.log.project.ProjectLogsUi
import org.example.ui.admin.project.*
import org.example.ui.common.authentication.LoginUi
import org.example.ui.common.authentication.RegisterUi
import org.example.ui.common.authentication.StartUpMenuUi
import org.example.ui.common.components.Colors
import org.example.ui.common.components.Printer
import org.example.ui.common.components.Reader
import org.example.ui.common.project.ProjectMateUi
import org.example.ui.common.project.ProjectStateSelectedUi
import org.example.ui.common.project.ProjectTasksUi
import org.example.ui.common.project.ViewProjectsUi
import org.example.ui.common.task.CreateTaskUi
import org.example.ui.common.task.EditTaskUi
import org.example.ui.mate.MateUi
import org.example.ui.mate.UserProjectsUi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.admin.log.task.TaskLogUi
import ui.admin.log.task.TaskLogsUi

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
}