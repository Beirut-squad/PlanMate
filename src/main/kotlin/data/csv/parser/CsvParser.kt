package org.example.data.csv.parser

interface CsvParser<T> {

    suspend fun parseLine(line: String): T?
    suspend fun parseFile(csvLines: List<String>): List<T>
}