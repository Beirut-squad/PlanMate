package domain.exception.handler

interface ExceptionHandler {
    suspend fun handle(exception: Throwable)

    suspend fun <T> runSafely(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Throwable) {
            handle(e)
            Result.failure(e)
        }
    }
}


