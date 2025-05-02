package data.csv.user_csv

import CsvParser
import io.mockk.mockk
import org.example.data.csv.CsvReader
import org.example.models.User
import org.junit.jupiter.api.Assertions.*
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.google.common.truth.Truth.assertThat
import creator_helper.createCsvLineForUser
import creator_helper.createCsvLineForUserInvalid
import io.mockk.every
import org.example.models.Role

class CsvReaderTest{

//  private lateinit var parser: CsvParser<User>
//  private lateinit var reader: CsvReader<User>

//  @BeforeTest
//  fun setup(){
//   parser = mockk()
//   reader = CsvReader<User>(parser)
//  }
//    @Test
//    fun `given valid CSV lines, when read called, then return parsed Users`() {
//        // given
//        val lines = listOf(
//            createCsvLineForUser(),
//            createCsvLineForUser()
//        )
//        val user1 = User(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ismail", "secret123", "ismail.elkalili@gmail.com", Role.ADMIN, false)
//        val user2 = User(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ismail", "secret123", "ismail.elkalili@gmail.com", Role.ADMIN, false)
//
//        every { parser.parseLine(lines[0]) } returns user1
//        every { parser.parseLine(lines[1]) } returns user2
//
//        // when
//        val result = reader.read(lines)
//
//        // then
//        assertThat(result).isEqualTo(listOf(user1, user2))
//    }
//
//    @Test
//    fun `given CSV lines with some invalid entries, when read called, then return only valid Users`() {
//        // given
//        val lines = listOf(
//            createCsvLineForUserInvalid(),
//            createCsvLineForUser()
//        )
//        val user = User(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ismail", "secret123", "ismail.elkalili@gmail.com", Role.ADMIN, false)
//
//        every { parser.parseLine(lines[0]) } returns null
//        every { parser.parseLine(lines[1]) } returns user
//
//        // when
//        val result = reader.read(lines)
//
//        // then
//        assertThat(result).isEqualTo(listOf(user))
//    }
//
//    @Test
//    fun `given all invalid CVS lines, when read called, then return empty list`() {
//        //given
//        val lines = listOf(
//            createCsvLineForUserInvalid(),
//            createCsvLineForUserInvalid()
//        )
//        every { parser.parseLine(lines[0]) } returns null
//
//        //when
//        val result = reader.read(lines)
//
//        //then
//        assertThat(result).isEmpty()
//    }
//    @Test
//    fun `given empty CSV lines, when read called, then return empty list`() {
//        // given
//        val lines = emptyList<String>()
//
//        // when
//        val result = reader.read(lines)
//
//        // then
//        assertThat(result).isEmpty()
//    }
//
}
