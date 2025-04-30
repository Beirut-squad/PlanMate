package data.csv.user_csv

import creator_helper.createCsvLineForUser
import creator_helper.createUserForCsvWriter
import creator_helper.createUserHelper
import org.example.data.csv.user_csv.UserCsvWriter
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
 }