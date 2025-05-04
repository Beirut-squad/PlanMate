package org.example.di

import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.ui.common.components.Reader
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.common.screens.ViewProjectLogsScreen
import org.example.ui.common.screens.ViewProjectsScreen
import org.example.ui.admin.home_screens.AdminHomeScreen
import org.example.ui.mate.home_screen.MateHomeScreen
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.example.ui.common.components.Colors
import org.example.ui.common.components.Viewer

val uiModule = module {
    singleOf(::AuthenticationMainScreen)
    singleOf(::LoginScreen)
    singleOf(::RegisterScreen)
    singleOf(::Colors)
    singleOf(::Viewer)
    singleOf(::Reader)
    singleOf(::AdminHomeScreen)
    singleOf(::MateHomeScreen)

    singleOf(::ViewProjectsScreen)
    singleOf(::ViewProjectLogsScreen)
    singleOf(::CreateNewProjectScreen)
    singleOf(::GetAllProjectsUseCases)
}