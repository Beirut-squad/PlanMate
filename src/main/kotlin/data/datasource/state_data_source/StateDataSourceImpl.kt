package org.example.data.datasource.state_data_source

import data.datasource.state_data_source.StateDataSource
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.models.State
import java.io.File
import java.util.UUID

class StateDataSourceImpl(
    private val writer: CsvWriter<State>,
    private val reader: CsvReader<State>,
    private val filePath: String,
) : StateDataSource {
    override fun createState(state: State): Result<State> {
        TODO("Not yet implemented")
    }

    override fun editState(state: State): Result<State> {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: UUID): Result<Unit> {
        TODO("Not yet implemented")
    }

}
