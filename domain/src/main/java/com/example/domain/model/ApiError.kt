package apps.hm.mhchars.domain.model

import java.io.IOException
import java.net.SocketTimeoutException

class ConnectionError(
    message: String = "Ошибка сети. Повторите попытку позже.",
    cause: IOException
) : Error(message, cause)

class TimeoutError(
    message: String = "Превышено время ожидания клиента",
    cause: SocketTimeoutException
) : Error(message, cause)