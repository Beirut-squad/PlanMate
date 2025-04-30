package org.example.data.csv

import java.util.*

interface CsvEditor<T> {
    fun add(item: T)
    fun update(item: T): Boolean
    fun deleteById(id: UUID): Boolean
}