package org.example

import org.example.data.csv.state_csv.StateCsvParser
import org.example.data.csv.task_csv_parser.TaskCsvParser

fun main() {
    val stateCsvParser: StateCsvParser = StateCsvParser()
    val taskCsvParser: TaskCsvParser = TaskCsvParser(stateCsvParser)

    val taskString = "[d1234567-89ab-cdef-0123-456789abcdef, d1234567-89ab-cdef-0123-456789abcdef ,Project Name,Project Description, , 5481551e-2b45-49a0-b5fc-123456789012 ,2024-04-01T12:00:00,2024-04-02T12:00:00]"

    val result = taskCsvParser.parseLine(taskString)
    println(result?.state)

}