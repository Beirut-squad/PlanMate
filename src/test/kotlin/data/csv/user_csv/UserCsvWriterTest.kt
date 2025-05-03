package data.csv.user_csv

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
import java.util.UUID


class UserCsvWriterTest{
  private lateinit var userCsvWriter :UserCsvWriter
  private lateinit var filePath: String

  @BeforeTest
  fun setUp(){
   userCsvWriter = UserCsvWriter()
   filePath = "test_users.csv"

  }

    @Test
    fun `given invalid file name when writeToFile is called then return failure`(){
        //given
        val invalidFilePath = "invalid|file/na|me.csv"
        val users   = listOf(createUserForCsvWriter())

        //when
        val result = userCsvWriter.writeToFile(users  , invalidFilePath)

        //then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `given user with empty name when isValidUser is called then should return false`() {
        // Given
        val user = User(id = UUID.randomUUID(), name = "", password = "password123", email = "ismail@example.com", isDeleted = false , role = Role.ADMIN)

        // When
        val result = userCsvWriter.isValidUser(user)

        // Then
        assertFalse(result)
    }

    @Test
    fun `given user with empty password when isValidUser is called then should return false`() {
        // Given
        val user = User(id = UUID.randomUUID(), name = "ismail", password = "", email = "ismail@example.com", isDeleted = false , role = Role.ADMIN)

        // When
        val result = userCsvWriter.isValidUser(user)
        // Then
        assertFalse(result)
    }

    @Test
    fun `given user with empty email when isValidUser is called then should return false`() {
        // Given
        val user = User(id = UUID.randomUUID(), name = "ismail", password = "password123", email = "", isDeleted = false , role = Role.ADMIN)

        // When
        val result = userCsvWriter.isValidUser(user)

        // Then
        assertFalse(result)
    }
    @Test
    fun `given valid user when isValidUser is called then should return true`() {
        // Given
        val user = User(id = UUID.randomUUID(), name = "ismail", password = "password123", email = "ismail@example.com", isDeleted = false , role = Role.ADMIN)

        // When
        val result = userCsvWriter.isValidUser(user)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isValidUser should return false when id is default UUID`() {
        val user = User(
            id = UUID(0, 0),
            name = "Ismail",
            password = "pass",
            email = "ismail@gmail.com",
            isDeleted = false,
            role = Role.ADMIN
        )
        assertFalse(userCsvWriter.isValidUser(user))
    }
    @Test
    fun `given non-empty file when writeToFile is called then header should not be written`() {
        val filePath = "test_file.csv"
        val file = File(filePath)
        file.writeText("id,name,password,email,role,isDeleted\n")

        val user = createUserForCsvWriter()
        val writer = UserCsvWriter()
        writer.writeToFile(listOf(user), filePath)

        val lines = file.readLines()
        assertTrue(lines[0].contains("id,name,password,email,role,isDeleted"))

        file.delete()
    }

 }