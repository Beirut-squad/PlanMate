package org.example.data.csv

import org.example.models.Log
import java.io.File

interface CsvWriter {
    fun appendLogToCsv(log: Log, file: File): Boolean
}