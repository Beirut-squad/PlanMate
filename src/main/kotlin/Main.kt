package org.example

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