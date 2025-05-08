package org.example.di

import org.example.ui.admin.project.EditProjectStateUi
import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.logic.use_cases.project_manegment.GetProjectByIdUseCase
import org.example.ui.admin.home_screen.AdminHomeScreen
import org.example.ui.admin.log.AllProjectsLogsView
import org.example.ui.admin.project.*
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.common.components.Colors
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.*
import org.example.ui.mate.home_screen.MateHomeUI
import org.example.ui.mate.home_screen.ViewProjectsForUserUI
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    singleOf(::AuthenticationMainScreen)
    singleOf(::LoginScreen)
    singleOf(::RegisterScreen)
    singleOf(::Colors)
    singleOf(::Viewer)
    singleOf(::Reader)
    singleOf(::AdminHomeScreen)
    singleOf(::MateHomeUI)
    singleOf(::ViewProjectsUI)
    singleOf(::ViewProjectLogsUI)
    singleOf(::CreateNewProjectScreen)
    singleOf(::GetAllProjectsUseCases)
    singleOf(::ViewProjectsForUserUI)
    singleOf(::ViewProjectForMateUI)
    singleOf(::ViewStateSelectedForProjectUI)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::CreateNewTaskUI)
    singleOf(::AllProjectsLogsView)
    singleOf(::SingleProjectScreen)
    singleOf(::EditProjectScreen)
    singleOf(::EditTaskUI)
    singleOf(::ViewAllTaskForProjectUI)
    singleOf(::ViewProjectsScreen)
    singleOf(::EditProjectStateUi)
    singleOf(::DeleteProjectStateUi)
    singleOf(::ViewProjectStatesUi)
    singleOf(::SingleStateUi)
    singleOf(::AddUserForProjectUI)
}