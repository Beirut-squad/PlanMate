package data.csv.state_csv

import creator_helper.createTestTaskState
import org.example.data.csv.state_csv.StateCsvWriter
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

     //then
     assertTrue(File(filePath).exists())


 }
 }