package org.example.di

import org.example.ui.Reader
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.example.ui.home_screens.ViewProjectsScreen
import org.example.ui.home_screens.HomeScreen
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.Colors
import ui.Viewer

val uiModule = module {
    singleOf(::AuthenticationMainScreen)
    singleOf(::LoginScreen)
    singleOf(::RegisterScreen)
    singleOf(::Colors)
    singleOf(::Viewer)
    singleOf(::Reader)
    singleOf(::HomeScreen)

    singleOf(::ViewProjectsScreen)
    singleOf(::ViewProjectLogsScreen)
    singleOf(::CreateNewProjectScreen)
}