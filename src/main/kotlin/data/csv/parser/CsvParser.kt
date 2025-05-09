package org.example.data.csv.parser

interface CsvParser<T> {

    fun parseLine(line: String): T?
    fun parseFile(csvLines: List<String>): List<T>
}