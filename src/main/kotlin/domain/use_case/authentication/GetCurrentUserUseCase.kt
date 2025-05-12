package domain.use_case.authentication

import domain.exception.handler.ExceptionHandler
import domain.model.User
import org.example.domain.repository.AuthenticationRepository

class GetCurrentUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val exceptionHandler: ExceptionHandler,

    ) {

    suspend fun getCurrentUser(): User {
        return exceptionHandler.tryCatchingAsyncWithResult(
            action = {
                authenticationRepository.getCurrentLoggedInUser()
            }
        )
    }
}