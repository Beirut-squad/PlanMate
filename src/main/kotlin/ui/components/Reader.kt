package ui.components

class Reader {
    fun readInput(): String? {
        return readlnOrNull()
    }
    fun readInt(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }
}