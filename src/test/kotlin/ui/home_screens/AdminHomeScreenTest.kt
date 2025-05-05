package ui.home_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.common.components.Reader
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.admin.home_screen.AdminHomeScreen
import org.example.ui.admin.log.AllProjectsLogsView
import org.junit.jupiter.api.Test
import org.example.ui.common.components.Viewer
import org.example.ui.common.screens.ViewProjectsForUserUI

class AdminHomeScreenTest {

    private val viewer: Viewer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val viewProjectsScreen = mockk<ViewProjectsForUserUI>(relaxed = true)
    private val createNewProjectScreen = mockk<CreateNewProjectScreen>(relaxed = true)
    private val allProjectsLogsView: AllProjectsLogsView = mockk(relaxed = true)

    private val adminHomeScreen = AdminHomeScreen(
        viewer,
        reader,
        viewProjectsScreen,
        createNewProjectScreen,
        allProjectsLogsView
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
    fun `should navigate to AllProjectsLogsView when option 3 is chosen`() {
        every { reader.readInt() } returns 3

        adminHomeScreen.show()

        verify { allProjectsLogsView.show() }
    }

    @Test
    fun `should show goodbye message and exit when option 3 is chosen`() {
        every { reader.readInt() } returns 4

        adminHomeScreen.show()

        verify(exactly = 1) { viewer.printGoodbyeMessage("Goodbye") }
    }
}