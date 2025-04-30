package org.example.logic.exceptions

import kotlin.Exception


class TaskCreationException(massage: String) : Exception(massage)

class TaskEditException(massage: String) : Exception(massage)

class TaskDeletionException(massage: String) : Exception(massage)

class TaskRetrievalException(massage: String) : Exception(massage)