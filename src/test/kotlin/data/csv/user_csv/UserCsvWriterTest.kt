package data.csv.user_csv

import creator_helper.createCsvLineForUser
import creator_helper.createUserForCsvWriter
import creator_helper.createUserForCsvWriterInvalid
import creator_helper.createUserHelper
import org.example.data.csv.user_csv.UserCsvWriter
import org.example.models.Role
import org.example.models.User
import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserCsvWriterTest{
  private lateinit var userCsvWriter :UserCsvWriter
  private lateinit var filePath: String

  @BeforeTest
  fun setUp(){
   userCsvWriter = UserCsvWriter()
   filePath = "test_users.csv"

  }

 @Test
 fun`given list of Users when writeToFile is called then file shoud be created`(){
  //given
  val user= createUserForCsvWriter()
  val  listOfUsers = listOf(user)

  //when
  userCsvWriter.writeToFile(listOfUsers,filePath)

  //then
  assertTrue(File(filePath).exists())

 }

 @Test
 fun `given empty list of Users when writeToFile is called then file shoud contain only header`(){
     //given
     val users = emptyList<User>()

     //then
     val result = userCsvWriter.writeToFile(users,filePath)
     val file = File(filePath)

     //then
     val content = file.readText()
     assertTrue(file.exists())
     assertTrue(content.contains("id,name,password,email,role,isDeleted"))
     assertTrue(content.lines().size == 2)
     assertTrue(content.lines()[1].isBlank())
 }

 @Test
 fun `given list of Users when writeToFile is called then file shoud be created and sotre users`(){
     //given
     val user  = createUserForCsvWriter()
     val mateUser  = createUserForCsvWriter(role = Role.MATE.toString())
     val deletedUser   = createUserForCsvWriter(isDeleted = true)
     val activeUser   = createUserForCsvWriter()
     val testUser   = createUserForCsvWriter(name = "testUser")

     val listOfUsers = listOf(user, mateUser , deletedUser  , testUser , activeUser)
     val filePath = "test_users.csv"

     //when
     val result = userCsvWriter.writeToFile(listOfUsers , filePath)

     //then
    assertTrue(result.isSuccess)
     val file = File(filePath)
    assertTrue(file.exists())
     val lines = file.readLines()
     assertTrue(lines.size == 6)
     assertTrue(lines[0].contains("id,name,password,email,role,isDeleted"))
     assertTrue(lines[1].contains("Ismail"))
     assertTrue(lines[2].contains(Role.MATE.toString()))
     assertTrue(lines[3].contains("true"))

     file.delete()

 }

    @Test
    fun `give list with invalid user when writeToFile is called then skip or handle invalid user`(){
        //given
        val userValid = createUserForCsvWriter()
        val userInvalid = createUserForCsvWriterInvalid()
        val users = listOf<User>(userValid, userInvalid)

        //when
        val result = userCsvWriter.writeToFile(users,filePath)

        //then
        val file = File(filePath)
        assertTrue(file.exists())
        val lines = file.readLines()
        assertTrue(lines[0].contains("id,name,password,email,role,isDeleted"))
        assertTrue(lines.size == 2)
        assertTrue(lines[1].contains("Ismail"))

        file.delete()
    }

 }