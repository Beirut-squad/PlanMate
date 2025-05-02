package data.csv.state_csv

import creator_helper.createTestTaskState
import creator_helper.createUserForCsvWriter
import org.example.data.csv.state_csv.StateCsvWriter
import org.example.models.State
import org.example.models.User
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class StateCsvWriterTest{
  private lateinit var stateCsvWriter: StateCsvWriter
  private lateinit var filePath: String

  @BeforeTest
  fun setup(){
   stateCsvWriter = StateCsvWriter()
   filePath = "test_state.csv"
  }

    @Test
    fun `given list of State when writeToFile is called then should be created`(){
  //Given
     val state = createTestTaskState(id= UUID.randomUUID(),name="To Do")
     val listOfState = listOf(state)

     //when
     stateCsvWriter.writeToFile(listOfState , filePath)
     val file = File(filePath)
     //then
     assertTrue(file.exists())

     file.delete()
 }
    @Test
    fun `given empty List Of State when writeToFile is called then should create file hava only header`(){
    //Given
        val state = emptyList<State>()

        //then
        val file = File(filePath)
        assertTrue(file.length() == 0L)
        stateCsvWriter.writeToFile(state, filePath)

        //then
        assertTrue(file.exists())
    }

    @Test
    fun `given list of state when writeToFile is called then should create file with header`(){
   //Given
    val listOfState = listOf(
        createTestTaskState(id= UUID.randomUUID(),name="To Do"),
        createTestTaskState(id= UUID.randomUUID(),name="In Processing")
    )

    //when
    stateCsvWriter.writeToFile(listOfState , filePath)
    val file = File(filePath)
    val lines = file.readLines()

    //then

    assertTrue(file.exists())
    assertTrue(lines[0].contains("id,name"))
    assertTrue(lines[1].contains("To Do"))
    assertTrue(lines[2].contains("In Processing"))
    assertTrue(lines.size == 3)

    file.delete()
}

    @Test
    fun `given list of state with invalid state when writeToFile is called then should create file and don't writer invalid state`(){
        //Given
        val listOfState = listOf(
            createTestTaskState(id= UUID.randomUUID(),name="To Do"),
            createTestTaskState(id= UUID.randomUUID(),name="")
        )
        //When
        val file = File(filePath)


        stateCsvWriter.writeToFile(listOfState , filePath)

        val lines = file.readLines()

        //then
        assertTrue(file.exists())
        assertTrue(lines[0].contains("id,name"))
        assertTrue(lines[1].contains("To Do"))
        assertTrue(lines.size == 2)

        file.delete()

    }
    @Test
    fun `given invalid file name when writeToFile is called then return failure`(){
        //given
        val filePath = "invalid|file/na|me.csv"
        val listOfState = listOf(
            createTestTaskState(id= UUID.randomUUID(),name="To Do")
        )

        //when
        val result = stateCsvWriter.writeToFile(listOfState , filePath)

        //then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
    @Test
    fun `isValidState returns false when name is blank`() {
        val state = State(UUID.randomUUID(), "")
        assertFalse(stateCsvWriter.isValidState(state))
    }
    @Test
    fun `isValidState returns false when id is default UUID (0, 0)`() {
        val state = State(UUID(0, 0), "To DO")
        assertFalse(stateCsvWriter.isValidState(state))
    }

    @Test
    fun `isValidState returns false when name is blank and id is default UUID (0, 0)`() {
        val state = State(UUID(0, 0), "")
        assertFalse(stateCsvWriter.isValidState(state))
    }
 }