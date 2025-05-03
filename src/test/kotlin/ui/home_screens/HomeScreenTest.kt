package ui.home_screens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.ui.Reader
import org.example.ui.home_screens.HomeScreen
import org.example.ui.home_screens.ViewProjectsScreen
import org.example.ui.home_screen.CreateNewProjectScreen
import org.example.ui.home_screens.ViewProjectLogsScreen
import org.junit.jupiter.api.Test
import ui.Viewer

class HomeScreenTest {

    private val viewer: Viewer = mockk(relaxed = true)
    private val reader: Reader = mockk(relaxed = true)
    private val viewProjectsScreen = mockk<ViewProjectsScreen>(relaxed = true)
    private val createNewProjectScreen = mockk<CreateNewProjectScreen>(relaxed = true)
    private val viewProjectLogsScreen = mockk<ViewProjectLogsScreen>(relaxed = true)

    private val homeScreen = HomeScreen(
        viewer,
        reader,
        viewProjectsScreen,
        createNewProjectScreen,
        viewProjectLogsScreen
    )

    @Test
    fun `should navigate to ViewProjectsScreen when option 1 is chosen`() {
        every { reader.readInt() } returns 1

        homeScreen.show()

        verify { viewProjectsScreen.show() }
    }

    @Test
    fun `should navigate to CreateNewProjectScreen when option 2 is chosen`() {
        every { reader.readInt() } returns 2

        homeScreen.show()

        verify { createNewProjectScreen.show() }
    }

    @Test
    fun `should navigate to ViewProjectLogsScreen when option 3 is chosen`() {
        every { reader.readInt() } returns 3

        homeScreen.show()

        verify { viewProjectLogsScreen.show() }
    }

    @Test
    fun `should display a goodbye message and exits when option 4 is chosen`() {
        every { reader.readInt() } returnsMany listOf(4, 1)

        homeScreen.show()

        verify(exactly = 1) { viewer.printGoodbyeMessage("Goodbye") }
    }
}