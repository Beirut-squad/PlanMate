package data.csv.log_for_task_csv

import creator_helper.createProjectLogHelper
import creator_helper.createTaskLogHelper
import org.example.data.csv.log_for_task_csv.LogCsvWriterForTask
import org.example.models.ProjectLog
import org.example.models.TaskLog
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class LogCsvWriterForTaskTest{
  private lateinit var logCsvWriterForTask: LogCsvWriterForTask
  private lateinit var filePath : String

  @BeforeTest
  fun setup(){
   logCsvWriterForTask = LogCsvWriterForTask()
   filePath = "log_for_task.csv"
  }

 @Test
 fun `given empty list of log Task when writeToFile is called then file sholud contain only header`(){
  //Given
  val listOfLogTask = emptyList<TaskLog>()
  val file = File(filePath)
  assertTrue(file.length() == 0L)

  //when
 logCsvWriterForTask.writeToFile(listOfLogTask,filePath)
  val content = file.readText()
  //Then
  assertTrue(file.exists())
  assertTrue(content.contains("id,userId,entityId,previousEntity,currentEntity,createdAt"))
  assertTrue(content.lines().size == 2)
  assertTrue(content.lines()[1].isBlank())
  file.delete()
 }
 @Test
 fun `given list of log Task when writeToFile is called then file should be created`(){
  //Given
  val listOfLogTask = listOf(createTaskLogHelper(), createTaskLogHelper())
  val file = File(filePath)

  //when
  logCsvWriterForTask.writeToFile(listOfLogTask,filePath)

  //then

  assertTrue(file.exists())
  file.delete()
 }


 @Test
 fun `given invalid file name when writeToFile is called then return failure`() {
  //Given
  val invalidFilePath = "invalid|file.csv"
  val listOfLogTask = listOf(createTaskLogHelper(), createTaskLogHelper())

  //when
  val result = logCsvWriterForTask.writeToFile(listOfLogTask,invalidFilePath)

  //then
  assertTrue(result.isFailure)
  assertTrue(result.exceptionOrNull() is IllegalArgumentException)
 }

 @Test
 fun `given log Task with invalid ID when isValidTaskLog is called then should return false`() {
  //Given
  val logTask = createTaskLogHelper(id = UUID(0,0))
  //When
  val result = logCsvWriterForTask.isValidTaskLog(logTask)

  assertFalse(result)
 }

 @Test
 fun `given log Task with invalid userId when isValidTaskLog is called then should return false`() {
  //Given
  val logTask = createTaskLogHelper(userId = UUID(0,0))
  //When
  val result = logCsvWriterForTask.isValidTaskLog(logTask)

  assertFalse(result)
 }

 @Test
 fun `given log Task with invalid entityId when isValidTaskLog is called then should return false`() {
  //Given
  val logTask = createTaskLogHelper(entityId = UUID(0,0))
  //When
  val result = logCsvWriterForTask.isValidTaskLog(logTask)

  assertFalse(result)
 }

 @Test
 fun `given log Task with empty previousEntity when isValidTaskLog is called then should return false`() {
  //Given
  val logTask = createTaskLogHelper(previousEntity = null)
  //When
  val result = logCsvWriterForTask.isValidTaskLog(logTask)

  assertTrue(result)
 }
 @Test
 fun `given log Task with empty currentEntity when isValidTaskLog is called then should return false`() {
  //Given
  val logTask = createTaskLogHelper(currentEntity = null)
  //When
  val result = logCsvWriterForTask.isValidTaskLog(logTask)

  assertTrue(result)
 }



}