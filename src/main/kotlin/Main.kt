package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.uiModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(
            uiModule,
            logicModule,
            dataModule
        )
    }

    println("Hello World!")
}