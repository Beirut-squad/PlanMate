package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.example.ui.common.authentication_screens.AuthenticationMainScreen
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

suspend fun main() {
    startKoin {
        modules(
            uiModule,
            logicModule,
            dataModule
        )
    }

    val authenticationMainScreen: AuthenticationMainScreen = getKoin().get()

    authenticationMainScreen.show()
}