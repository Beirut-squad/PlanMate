package org.example.data.csv.helper

 fun isValidFileName(fileName: String): Boolean{
    val invalidChars = "[\\\\/:*?\"<>|]".toRegex()
    return !invalidChars.containsMatchIn(fileName) && fileName.length <= 255
}