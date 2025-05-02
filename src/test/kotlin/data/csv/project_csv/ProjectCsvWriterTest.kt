package data.csv.project_csv

import creator_helper.createProjectHelper
import creator_helper.createUserForCsvWriter
import creator_helper.createUserHelper
import org.example.data.csv.project_csv_parser.ProjectCsvWriter
import org.example.models.Project
import org.example.models.Role
import org.example.models.State
import org.example.models.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProjectCsvWriterTest{
 private lateinit var projectCsvWriter : ProjectCsvWriter
 private lateinit var filePath: String

 @BeforeTest
 fun setup(){
  projectCsvWriter = ProjectCsvWriter()
  filePath = "test_project.csv"
 }

 @Test
 fun `given list of project when writeToFile is called then should be created`() {
  //Given
  val projects = createProjectHelper()
  val listOfProjects = listOf(projects)

  //when
  projectCsvWriter.writeToFile(listOfProjects, filePath)
  val file = File(filePath)

  //then
  assertTrue(file.exists())

  file.delete()
 }

 @Test
 fun `given empty List Of Project when writeToFile is called then should not create file`(){
//Given
  val listOfProjects = emptyList<Project>()

  //When
  projectCsvWriter.writeToFile(listOfProjects, filePath)
  val file = File(filePath)

  //then
  assertTrue(file.exists())

  file.delete()
 }

 @Test
 fun `given list of Project when writeToFile is called then should create file with header`(){
  //Given
  val listOfProject = listOf(
   createProjectHelper(),
   createProjectHelper()
  )

  println(listOfProject[1])
  //when
  projectCsvWriter.writeToFile(listOfProject , filePath)
  val file = File(filePath)
  val lines = file.readLines()

  //then
  assertTrue(file.exists())
  assertTrue(lines[0].contains("id,name,description,creatorUserID,createdAt,updatedAt,state"))
  assertTrue(lines[1].contains("Test Project Name"))
  assertTrue(lines[2].contains("Test Project Description"))
  assertTrue(lines.size == 3)

  file.delete()
 }

 @Test
 fun `given list of Project with invalid state when writeToFile is called then should create file and don't writer invalid Project`(){
  //Given
  val listOfProject = listOf(
   createProjectHelper(),
   createProjectHelper(name = "")
  )
  //When
  projectCsvWriter.writeToFile(listOfProject , filePath)
  val file = File(filePath)
  val lines = file.readLines()

  //then
  assertTrue(file.exists())
  assertTrue(lines[0].contains("id,name,description,creatorUserID,createdAt,updatedAt,state"))
  assertTrue(lines[1].contains("Test Project Name"))
  assertTrue(lines.size == 2)

  file.delete()

 }

// @Disabled
// @Test
// fun `given file with existing content when writeToFile is called then old content should be preserved and new content appended`(){
// //Given
//  val existingContent = "[id,name,description,creatorUserID,createdAt,updatedAt,state]\n[03fcf25f-c810-4280-9487-2dd1665b00a0,Test Project Name,Test Project Description,2025-05-01T16:47:36.661,2025-05-01T16:47:36.661,[State(id=b97fe43d-bee4-4ed3-b142-076935241786, name=To Do)]]\n"
//  val file = File(filePath)
//  file.writeText(existingContent)
//
//  val listOfProject = listOf(createProjectHelper())
//
//  //when
//  projectCsvWriter.writeToFile(listOfProject , filePath)
//  val lines = file.readLines()
//  //then
//  assertTrue(file.exists())
//  assertTrue(lines[0].contains("id,name,description,creatorUserID,createdAt,updatedAt,state"))
//  assertTrue(lines[2].contains("Test Project Name"))
//
// }
 @Test
 fun `given invalid file name when writeToFile is called then return failure`(){
  //given
  val invalidFilePath = "invalid|file/na|me.csv"
  val listOfProject   = listOf(createProjectHelper())

  //when
  val result = projectCsvWriter.writeToFile(listOfProject , invalidFilePath)

  //then
  assertTrue(result.isFailure)
  assertTrue(result.exceptionOrNull() is IllegalArgumentException)
 }

 @Test
 fun `isValidProject should return false when id is default UUID`() {
  val project = Project(
   id = UUID(0, 0), // invalid
   name = "new name",
   description = "new description",
   creatorUserID = UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   state = listOf(State(UUID.randomUUID(),"To Do"))
  )
  assertFalse(projectCsvWriter.isValidProject(project))
 }

 @Test
 fun `isValidProject should return false when name is Blank`() {
  val project = Project(
   id = UUID.randomUUID(), // invalid
   name = "",
   description = "new description",
   creatorUserID = UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   state = listOf(State(UUID.randomUUID(),"To Do"))
  )
  assertFalse(projectCsvWriter.isValidProject(project))
 }
 @Test
 fun `isValidProject should return false when description is Blank`() {
  val project = Project(
   id = UUID.randomUUID(), // invalid
   name = "new name",
   description = "",
   creatorUserID = UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   state = listOf(State(UUID.randomUUID(),"To Do"))
  )
  assertFalse(projectCsvWriter.isValidProject(project))
 }

 @Test
 fun `isValidProject should return false when creatorUserID is Blank`() {
  val project = Project(
   id = UUID.randomUUID(), // invalid
   name = "new name",
   description = "new description",
   creatorUserID = UUID(0,0),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   state = listOf(State(UUID.randomUUID(),"To Do"))
  )
  assertFalse(projectCsvWriter.isValidProject(project))
 }

 @Test
 fun `isValidProject should return false when state is empty`() {
  val project = Project(
   id = UUID.randomUUID(),
   name = "new name",
   description = "new description",
   creatorUserID =UUID.randomUUID(),
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now(),
   state = emptyList()
  )
  assertFalse(projectCsvWriter.isValidProject(project))
 }


}
