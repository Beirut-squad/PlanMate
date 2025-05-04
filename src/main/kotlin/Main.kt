package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.example.logic.exceptions.ErrorHandler
import org.example.ui.authentication_screens.AuthenticationMainScreen
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    startKoin {
        modules(
            uiModule,
            logicModule,
            dataModule
        )
    }
    try {
        val authenticationMainScreen: AuthenticationMainScreen = getKoin().get()
        authenticationMainScreen.show()
    } catch (error: Exception) {
        ErrorHandler().handle(error)
    }
}