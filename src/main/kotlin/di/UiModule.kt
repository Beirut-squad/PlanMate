package org.example.di

import org.example.logic.use_cases.project_manegment.GetAllProjectsUseCases
import org.example.ui.Reader
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.example.ui.home_screens.ViewProjectsScreen
import org.example.ui.home_screens.admin.ui.home_screens.admin.AdminHomeScreen
import org.example.ui.home_screens.mate.ui.home_screens.mate.MateHomeScreen
import org.example.ui.utils.ConsoleInputHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.example.ui.utils.InputHandler

import ui.Colors
import ui.Viewer

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
    single<InputHandler> { ConsoleInputHandler }
}