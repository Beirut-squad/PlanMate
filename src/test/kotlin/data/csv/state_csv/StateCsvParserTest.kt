package data.csv.state_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createTestTaskState
import org.example.data.csv.state_csv.StateCsvParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class StateCsvParserTest {


    private lateinit var stateCsvParser: StateCsvParser


    @BeforeEach
    fun setup() {
        stateCsvParser = StateCsvParser()
    }

    @Test
    fun `parseLine should return empty state when the string is empty `() {

        // When
        val result = stateCsvParser.parseLine("[      ]")

        // Then
        assertThat(result).isNull()

    }

    @Test
    fun `parseLine should return a state object when the string is not empty `() {
        // When
        val result = stateCsvParser.parseLine("[5481551e-2b45-49a0-b5fc-123456789012 , name]")

        // Then
        assertThat(result).isEqualTo(
            createTestTaskState(
                UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"),
                name = "name"
            )
        )
    }

    @Test
    fun ` parseFile should return empty list if there is no csv lines`() {

        // When
        val result = stateCsvParser.parseFile(emptyList())

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return a list of states when csv contains lines`() {

        // When
        val result = stateCsvParser.parseFile(
            listOf(
                "[5481551e-2b45-49a0-b5fc-123456789012 , name]",
                "[5481551e-2b45-49a0-b5fc-123456789012 , name]",
                "[5481551e-2b45-49a0-b5fc-123456789012 , name]",
                "[    ]",
                "[5481551e-2b45-49a0-b5fc-123456789012 , name]"
            )
        )

        // Then
        assertThat(result).isEqualTo(
            listOf(
                createTestTaskState(UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"), name = "name"),
                createTestTaskState(UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"), name = "name"),
                createTestTaskState(UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"), name = "name"),
                createTestTaskState(UUID.fromString("5481551e-2b45-49a0-b5fc-123456789012"), name = "name")

            )
        )


    }


}