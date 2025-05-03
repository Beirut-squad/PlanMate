package ui.home_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.Reader
import org.example.ui.home_screens.ViewProjectsScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.example.ui.home_screens.admin.ui.home_screens.admin.AdminHomeScreen
import org.junit.jupiter.api.Test
import ui.Viewer

class AdminHomeScreenTest {

    private val viewer: Viewer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val viewProjectsScreen = mockk<ViewProjectsScreen>(relaxed = true)
    private val createNewProjectScreen = mockk<CreateNewProjectScreen>(relaxed = true)

    private val adminHomeScreen = AdminHomeScreen(
        viewer,
        reader,
        viewProjectsScreen,
        createNewProjectScreen,
    )

    @Test
    fun `should navigate to ViewProjectsScreen when option 1 is chosen`() {
        every { reader.readInt() } returns 1

        adminHomeScreen.show()

        verify { viewProjectsScreen.show() }
    }

    @Test
    fun `should navigate to CreateNewProjectScreen when option 2 is chosen`() {
        every { reader.readInt() } returns 2

        adminHomeScreen.show()

        verify { createNewProjectScreen.show() }
    }

    @Test
    fun `should show goodbye message and exit when option 3 is chosen`() {
        every { reader.readInt() } returns 3

        adminHomeScreen.show()

        verify(exactly = 1) { viewer.printGoodbyeMessage("Goodbye") }
    }
}