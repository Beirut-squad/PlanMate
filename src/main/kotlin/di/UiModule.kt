package org.example.di

import org.example.ui.admin.project.EditProjectStateUi
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.ui.admin.home_screen.AdminHomeUi
import org.example.ui.admin.log.project.AllProjectLogsView
import org.example.ui.admin.project.*
import org.example.ui.common.authentication.AuthenticationMainUi
import org.example.ui.common.authentication.LoginUi
import org.example.ui.common.authentication.RegisterUi
import org.example.ui.common.components.Colors
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.example.ui.mate.MateHomeUi
import org.example.ui.mate.ViewProjectsForUserUi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.example.ui.admin.log.project.DisplayProjectLog
import org.example.ui.common.project.ViewAllTaskForProjectUi
import org.example.ui.common.project.ViewProjectForMateUi
import org.example.ui.common.project.ViewStateSelectedForProjectUi
import org.example.ui.common.task.CreateNewTaskUi
import org.example.ui.common.task.EditTaskUi

val uiModule = module {
    singleOf(::AuthenticationMainUi)
    singleOf(::LoginUi)
    singleOf(::RegisterUi)
    singleOf(::Colors)
    singleOf(::Viewer)
    singleOf(::Reader)
    singleOf(::AdminHomeUi)
    singleOf(::MateHomeUi)
    singleOf(::CreateNewProjectUi)
    singleOf(::GetAllProjectsUseCases)
    singleOf(::ViewProjectsForUserUi)
    singleOf(::ViewProjectForMateUi)
    singleOf(::ViewStateSelectedForProjectUi)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::CreateNewTaskUi)
    singleOf(::AllProjectLogsView)
    singleOf(::SingleProjectUi)
    singleOf(::EditProjectUi)
    singleOf(::EditTaskUi)
    singleOf(::ViewAllTaskForProjectUi)
    singleOf(::ViewProjectsUi)
    singleOf(::EditProjectStateUi)
    singleOf(::DeleteProjectStateUi)
    singleOf(::ViewProjectStatesUi)
    singleOf(::SingleStateUi)
    singleOf(::AddUserForProjectUi)
    singleOf(::DisplayProjectLog)
}