package domain.exception.handler

interface ExceptionHandler {
    suspend fun handle(exception: Throwable)

    suspend fun <T> tryCatchingAsyncWithResult(
        action: suspend () -> T,
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (Throwable) -> Unit = {},
        completion: () -> Unit = {}
    ): T {
        val result = executeWithHandling(action, onSuccess, onError)
        completion()
        return result
    }

    suspend fun tryCatchingAsync(
        action: suspend () -> Unit,
//        onSuccess: suspend () -> Unit = {},
        onError: suspend (Throwable) -> Unit = {},
        completion: () -> Unit = {}
    ) {
        executeWithHandling(action = action, onError = onError)
        completion()
    }

    private suspend fun <T> executeWithHandling(
        action: suspend () -> T,
        onSuccess: suspend ((T) -> Unit),
        onError: suspend (Throwable) -> Unit
    ): T {
        try {
            val result = action()
            onSuccess(result)
            return result
        } catch (exception: Throwable) {
            handleWithNotify(exception, onError)
            throw exception
        }
    }

    private suspend fun executeWithHandling(
        action: suspend () -> Unit,
//        onSuccess: suspend () -> Unit = {},
        onError: suspend (Throwable) -> Unit
    ) {
        try {
            action()
//            onSuccess()
        } catch (exception: Throwable) {
            handleWithNotify(exception, onError)
            throw exception
        }
    }

    private suspend fun handleWithNotify(
        exception: Throwable,
        onError: suspend (Throwable) -> Unit
    ) {
        handle(exception)
        onError(exception)
    }
}


