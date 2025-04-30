package org.example.data.csv

import java.util.UUID

interface CsvEditor<T> {
    fun add(item: T)
    fun update(item: T): Boolean
    fun deleteById(id: UUID): Boolean
}