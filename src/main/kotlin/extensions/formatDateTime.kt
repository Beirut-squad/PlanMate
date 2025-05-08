package extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a")
    return this.format(formatter)
}