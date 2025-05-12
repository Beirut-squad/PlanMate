import di.dataModule
import di.logicModule
import di.uiModule
import ui.view.authentication.StartUpMenuUi
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

    val startUpMenuUi: StartUpMenuUi = getKoin().get()

    startUpMenuUi.show()
}