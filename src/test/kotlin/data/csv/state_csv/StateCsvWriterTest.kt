package data.csv.state_csv

import creator_helper.createTestTaskState
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
    fun `given empty List Of State when writeToFile is called then should not create file`(){
    //Given
        val state = emptyList<State>()

        //then
        stateCsvWriter.writeToFile(state, filePath)
        val file = File(filePath)

        //then
        assertTrue(!file.exists())
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
        stateCsvWriter.writeToFile(listOfState , filePath)
        val file = File(filePath)
        val lines = file.readLines()

        //then
        assertTrue(file.exists())
        assertTrue(lines[0].contains("id,name"))
        assertTrue(lines[1].contains("To Do"))
        assertTrue(lines.size == 2)

        file.delete()

    }
 }