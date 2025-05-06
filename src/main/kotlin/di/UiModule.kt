package org.example.di

import org.example.ui.common.components.Reader
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.example.ui.authentication_screens.LoginScreen
import org.example.ui.authentication_screens.RegisterScreen
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.common.screens.ViewProjectLogsScreen
import org.example.ui.admin.project.ViewProjectsScreen
import org.example.ui.admin.log.AllProjectsLogsView
import org.example.ui.admin.home_screen.AdminHomeScreen
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
    singleOf(::AllProjectsLogsView)
}