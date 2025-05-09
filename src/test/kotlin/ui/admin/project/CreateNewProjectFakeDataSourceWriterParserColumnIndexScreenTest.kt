package ui.admin.project
import io.mockk.*
import org.example.logic.use_cases.project_manegment.CreateProjectUseCase
import org.example.ui.admin.project.CreateNewProjectScreen
import org.example.ui.common.components.Reader
import org.example.ui.common.components.Printer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
class CreateNewProjectFakeDataSourceWriterParserColumnIndexScreenTest {
    private val reader: Reader = mockk()
    private val printer: Printer = mockk(relaxed = true) // Made relaxed to simplify
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private lateinit var createNewProjectScreen: CreateNewProjectScreen
    @BeforeEach
    fun setup() {
        clearAllMocks()
        createNewProjectScreen = CreateNewProjectScreen(printer, reader, createProjectUseCase)
    }

    @Test

    fun `should create a project successfully`() {
        // Given
        val name = "Test Project"
        val description = "Test Description"
        every { reader.readInput() } returnsMany listOf(name, description)
        // When
        createNewProjectScreen.show()
        // Then
        verifySequence {
            printer.printTitle("Let's create a project")
            printer.printInfoLine("Write your project name")
            reader.readInput()
            printer.printOption("Tell me more about description of your project")
            reader.readInput()
        }
        val uuidSlot = slot<UUID>()
        val nameSlot = slot<String>()
        val descSlot = slot<String>()
        val statesSlot = slot<List<String>>()
        verify {
            createProjectUseCase.createProject(
                capture(uuidSlot),
                capture(nameSlot),
                capture(descSlot),
                capture(statesSlot)
            )
        }
        assertEquals(name, nameSlot.captured)
        assertEquals(description, descSlot.captured)
        assertEquals(emptyList<String>(), statesSlot.captured) // Matches the emptyList in production code
        assertNotNull(uuidSlot.captured)
    }

    @Test

    fun `should show correct prompts in order`() {
        // Given
        every { reader.readInput() } returns "test"
        // When
        createNewProjectScreen.show()
        // Then
        verifyOrder {
            printer.printTitle("Let's create a project")
            printer.printInfoLine("Write your project name")
            reader.readInput()
            printer.printOption("Tell me more about description of your project")
            reader.readInput()
        }
    }

    @Test

    fun `should generate new UUID for each project`() {
        // Given
        every { reader.readInput() } returns "test"
        // When
        createNewProjectScreen.show()
        createNewProjectScreen.show()
        // Then
        val uuids = mutableListOf<UUID>()
        verify(exactly = 2) {
            createProjectUseCase.createProject(
                capture(uuids),
                any(),
                any(),
                any()
            )
        }
        assertEquals(2, uuids.size)
        assertNotEquals(uuids[0], uuids[1])
    }
}