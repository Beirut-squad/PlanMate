package domain.exception

import org.example.ui.constants.StringConstants


class NullInputException(message: String = StringConstants.General.INVALID_INPUT) : IllegalStateException(message)