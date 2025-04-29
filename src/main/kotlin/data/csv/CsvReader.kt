package org.example.data.csv

import CsvParser

class CsvReader<T>(private val parser: CsvParser<T>){

fun read(csvLines:List<String>): List<T>{
    return emptyList()
    }
}

