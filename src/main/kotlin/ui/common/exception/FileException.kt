package ui.common.exception

open class FileException : Exception()

class InvalidFileNameException : FileException()

class InvalidDataFileException : FileException()

class EmptyCSVFileException : FileException()

class CsvValidationException : FileException()