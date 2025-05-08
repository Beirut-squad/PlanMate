package org.example.data.datasource.state_data_source

import data.datasource.state_data_source.StateDataSource
import org.example.data.csv.CsvReader
import org.example.data.csv.CsvWriter
import org.example.models.State
import java.util.UUID

class StateDataSourceImpl(
    private val writer: CsvWriter<State>,
    private val reader: CsvReader<State>,
    private val filePath: String,
) : StateDataSource {
    override fun createState(state: State): State{

        val states = reader.read(filePath)
        val newList = states + state
        writer.writeToFile(newList, filePath)
        return state
    }

    override fun editState(state: State): State {

        val states = reader.read(filePath)
        val updatedStates = states.map {
            if (it.id == state.id) state else it
        }

        if (states.none { it.id == state.id }) {
            throw IllegalStateException("State not found")
        }

        writer.writeToFile(updatedStates, filePath)
        return state
    }

    override fun deleteState(id: UUID): Unit {

        val states = reader.read(filePath)
        if (states.none { it.id == id }) {
            throw IllegalStateException("State not found")
        }

        val updatedStates = states.filterNot { it.id == id }
        writer.writeToFile(updatedStates, filePath)
    }
}

