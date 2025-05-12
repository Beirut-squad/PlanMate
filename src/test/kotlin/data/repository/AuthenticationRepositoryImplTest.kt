package data.repository

import creator_helper.createUserHelper
import data.datasource.interfaces.AuthenticationDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {
    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var authenticationRepository: AuthenticationRepositoryImpl
    private val testUser = createUserHelper()

    @BeforeEach
    fun setup() {
        authenticationDataSource = mockk()
        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Test
    fun `login should return user when credentials are valid`() = runTest {
        coEvery { authenticationDataSource.login(any(), any()) } returns testUser
        val result = authenticationRepository.login("test@test.com", "password123")
        assertEquals(testUser, result)
        coVerify { authenticationDataSource.login("test@test.com", "password123") }
    }

    @Test
    fun `checkEmail should validate email through datasource`() = runTest {
    coEvery { authenticationDataSource.isValidEmail(any()) } returns mockk()
        authenticationRepository.checkEmail("test@test.com")
        coVerify { authenticationDataSource.isValidEmail("test@test.com") }
    }

    
    @Test
    fun `registerMate should return registered user`() = runTest {
        coEvery {
            authenticationDataSource.registerMate(any(), any(), any())
        } returns testUser
        val result = authenticationRepository.registerMate("Test User", "password123", "test@test.com")
        assertEquals(testUser, result)
        coVerify {
            authenticationDataSource.registerMate(name = "Test User", password = "password123", email = "test@test.com")
        }
    }

    
    @Test
    fun `registerAdmin should return registered admin user`() = runTest {
        coEvery {
            authenticationDataSource.registerAdmin(any(), any(), any())
        } returns testUser
        val result = authenticationRepository.registerAdmin("Test User", "password123", "test@test.com")
        assertEquals(testUser, result)
        coVerify {
            authenticationDataSource.registerAdmin(
                email = "test@test.com",
                password = "password123",
                name = "Test User"
            )
        }
    }

    @Test
    fun `logout should call datasource`() = runTest {
        coEvery { authenticationDataSource.logout() } returns Unit
        authenticationRepository.logout()
        coVerify { authenticationDataSource.logout() }
    }

    @Test
    fun `isFirstRegister should return boolean from datasource`() = runTest {
        coEvery { authenticationDataSource.isFirstRegister() } returns true
        val result = authenticationRepository.isFirstRegister()
        assertTrue(result)
        coVerify { authenticationDataSource.isFirstRegister() }
    }

    @Test
    fun `getCurrentLoggedInUser should return current user`() = runTest {
        coEvery { authenticationDataSource.getCurrentUser() } returns testUser
        val result = authenticationRepository.getCurrentLoggedInUser()
        assertEquals(testUser, result)
        coVerify { authenticationDataSource.getCurrentUser() }
    }

    @Test
    fun `getUsers should return list of users`() = runTest {
        val usersList = listOf(testUser)
        coEvery { authenticationDataSource.getUsers() } returns usersList
        val result = authenticationRepository.getUsers()
        assertEquals(usersList, result)
        coVerify { authenticationDataSource.getUsers() }
    }
}