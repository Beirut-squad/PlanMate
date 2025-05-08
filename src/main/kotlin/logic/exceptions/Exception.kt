package org.example.logic.exceptions

import org.example.constants.StringConstants


class NullInputException(message: String = StringConstants.General.INVALID_INPUT) : IllegalStateException(message)