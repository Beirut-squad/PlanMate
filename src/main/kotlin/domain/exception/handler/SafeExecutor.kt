package domain.exception.handler

class SafeExecutor {
    suspend fun <T> tryToExecute(
        action: suspend () -> T,
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (Throwable) -> Unit = {},
        completion: () -> Unit = {}
    ) {
        try {
            onSuccess(action())
        } catch (exception: Throwable) {
            onError(exception)
        } finally {
            completion()
        }
    }
}