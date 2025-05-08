package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.example.ui.common.authentication.AuthenticationMainUi
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

    val authenticationMainUi: AuthenticationMainUi = getKoin().get()

    authenticationMainUi.show()
}