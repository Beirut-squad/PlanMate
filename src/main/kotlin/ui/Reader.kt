package org.example.ui

class Reader {
    fun readInput(): String? {
        return readlnOrNull()
    }
    fun readInt(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }
}