package org.example.data.csv.log_csv_parser

import org.example.data.csv.CsvWriter
import org.example.models.Log
import java.io.File

class LogCsvWriter: CsvWriter {
    override fun appendLogToCsv(log: Log, file: File): Boolean {
        TODO("Not yet implemented")
    }

}