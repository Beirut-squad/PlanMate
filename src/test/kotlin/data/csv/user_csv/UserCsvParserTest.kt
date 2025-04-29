package data.csv.user_csv

import com.google.common.truth.Truth.assertThat
import creator_helper.createCsvLineForUser
import creator_helper.createCsvLineForUserInvalid

import org.example.data.csv.user_csv.UserCsvParser
import org.example.models.Role
import org.example.models.User

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import java.util.*


class UserCsvParserTest
{
 private lateinit var userCvsParser : UserCsvParser
 @BeforeEach
 fun setUp(){
  userCvsParser = UserCsvParser()
 }

 @Test
 fun `given valid CSV line , when parserLine called , then return User`(){

  //given
 val csvLine = createCsvLineForUser()
  //when
 val result = userCvsParser.parseLine(csvLine)

  //then
  assertThat(result).isEqualTo(
   User(
     id =UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
     name = "Ismail",
     password = "secret123",
     email= "ismail.elkalili@gmail.com",
     role = Role.ADMIN,
     isDeleted = false
   )
  )
 }

    @Test
    fun `given CSV line with missing fields , when parseLine called , then return null`() {
        // given
        val csvLine = createCsvLineForUserInvalid()

        // when
        val result = userCvsParser.parseLine(csvLine)

        // then
        assertThat(result).isNull()
    }

@Test
    fun `given empty CSV line , when parseLine called , then return null`() {
        // given
        val csvLine = ""

        // when
        val result = userCvsParser.parseLine(csvLine)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `given CSV line with different role , when parseLine called , then return User with correct role`() {
        // given
        val csvLine = "550e8400-e29b-41d4-a716-446655440000,Ismail,secret123,ismail.elkalili@gmail.com,MATE,false"

        // when
        val result = userCvsParser.parseLine(csvLine)
        println(result)
        // then
        assertThat(result).isEqualTo(
            User(
                id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                name = "Ismail",
                password = "secret123",
                email = "ismail.elkalili@gmail.com",
                role = Role.MATE,
                isDeleted = false
            )
        )
    }

    @Test
    fun `given multiple valid CSV lines , when parseFile called , then return list of Users`() {
        // given
        val csvLines = listOf(
            "550e8400-e29b-41d4-a716-446655440000,Ismail,secret123,ismail.elkalili@gmail.com,ADMIN,false",
            "550e8400-e29b-41d4-a716-446655440001,Ahmed,secret456,ahmed@example.com,MATE,true"
        )

        // when
        val result = userCvsParser.parseFile(csvLines)

        // then
        assertThat(result).isEqualTo(
            listOf(
                User(
                    id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                    name = "Ismail",
                    password = "secret123",
                    email = "ismail.elkalili@gmail.com",
                    role = Role.ADMIN,
                    isDeleted = false
                ),
                User(
                    id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                    name = "Ahmed",
                    password = "secret456",
                    email = "ahmed@example.com",
                    role = Role.MATE,
                    isDeleted = true
                )
            )
        )
    }


}