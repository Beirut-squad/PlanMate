package data.datasource.mongo.implementation

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import com.mongodb.client.result.UpdateResult
import creator_helper.createTaskHelper
import data.datasource.mongo.mapper.toDocument
import data.datasource.mongo.mongo_db_connection.MongoConnection
import ui.common.exception.TaskDeletionFailedException
import ui.common.exception.TaskEditFailedException
import ui.common.exception.TaskNotFoundException
import io.mockk.*
import org.bson.Document
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskMongoDataSourceImplTest {
    val mongoConnection: MongoConnection = mockk(relaxed = true)
    val tasksCollection: MongoCollection<Document> = mockk(relaxed = true)
    private lateinit var taskMongoDataSource: TaskMongoDataSourceImpl

    @BeforeEach
    fun setUp() {
        every { mongoConnection.tasks } returns tasksCollection
        taskMongoDataSource = TaskMongoDataSourceImpl(mongoConnection)
    }

    @Test
    fun `should create task successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val taskDocument = task.toDocument()
        coEvery { tasksCollection.insertOne(taskDocument) }returns mockk()

        // when
        taskMongoDataSource.createTask(task)

        // then
        coVerify(exactly = 1) { tasksCollection.insertOne(taskDocument) }
    }


    @Test
    fun `should edit task successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val result = mockk<UpdateResult>()
        every { result.modifiedCount } returns 1
        coEvery { tasksCollection.replaceOne(any(), any()) } returns result

        // when
        taskMongoDataSource.editTask(task)

        // then
        coVerify(exactly = 1) { tasksCollection.replaceOne(any(), task.toDocument()) }
    }

    @Test
    fun `should throw TaskEditFailedException when no document was modified`() = runTest {
        // given
        val task = createTaskHelper()
        val result = mockk<UpdateResult>()
        every { result.modifiedCount } returns 0
        coEvery { tasksCollection.replaceOne(any(), any()) } returns result

        // when/then
        assertThrows<TaskEditFailedException> {
            runBlocking { taskMongoDataSource.editTask(task) }
        }
    }

    @Test
    fun `should delete task document from mongo collection`() = runTest {
        // given
        val taskId = UUID.randomUUID()
        coEvery { tasksCollection.findOneAndDelete(any()) } returns Document()

        // when
        taskMongoDataSource.deleteTask(taskId)

        // then
        coVerify(exactly = 1) { tasksCollection.findOneAndDelete(any()) }
    }

    @Test
    fun `should throw TaskDeletionFailedException when no document was found`() = runTest {
        // given
        val taskId = UUID.randomUUID()
        coEvery { tasksCollection.findOneAndDelete(any()) } returns null

        // when/then
        assertThrows<TaskDeletionFailedException> {
            runBlocking { taskMongoDataSource.deleteTask(taskId) }
        }
    }
    
    @Test
    fun `should throw TaskNotFoundException when no document was found`() = runTest {
        // given
        val taskId = UUID.randomUUID()
        coEvery { tasksCollection.find() } returns mockk {
            every { firstOrNull() } returns null
        }

        // when/then
        assertThrows<TaskNotFoundException> {
            runBlocking { taskMongoDataSource.getTask(taskId) }
        }
    }


    @Test
    fun `should get task successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val taskDocument = task.toDocument()
        val filter = Document("_id", task.id.toString())

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true
        every { cursorMock.next() } returns taskDocument

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find(filter) } returns findIterableMock

        // when
        val result = taskMongoDataSource.getTask(task.id)

        // then
        assertEquals(task.id, result.id)
        verify(exactly = 1) { tasksCollection.find(filter) }
    }

    @Test
    fun `should get all tasks successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val taskDocument = task.toDocument()

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns taskDocument

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find() } returns findIterableMock

        // when
        val result = taskMongoDataSource.getAllTasks()

        // then
        assertEquals(1, result.size)
        assertEquals(task.id, result[0].id)
        verify(exactly = 1) { tasksCollection.find() }
    }

    @Test
    fun `should throw TaskNotFoundException when getAllTasks finds null document`() = runTest {
        // given
        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns null
        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find() } returns findIterableMock

        // when/then
        assertThrows<TaskNotFoundException> {
            runBlocking { taskMongoDataSource.getAllTasks() }
        }
    }

    @Test
    fun `should get tasks by state and project ids successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val taskDocument = task.toDocument()
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val filter = Document("projectId", projectId.toString())
            .append("_id", stateId.toString())

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns taskDocument

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find(filter) } returns findIterableMock

        // when
        val result = taskMongoDataSource.getTasksByStateAndProjectIds(projectId, stateId)

        // then
        assertEquals(1, result.size)
        assertEquals(task.id, result[0].id)
        verify(exactly = 1) { tasksCollection.find(filter) }
    }

    @Test
    fun `should throw TaskNotFoundException when getTasksByStateAndProjectIds finds null document`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val filter = Document("projectId", projectId.toString())
            .append("_id", stateId.toString())

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns null

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find(filter) } returns findIterableMock

        // when/then
        assertThrows<TaskNotFoundException> {
            runBlocking { taskMongoDataSource.getTasksByStateAndProjectIds(projectId, stateId) }
        }
    }

    @Test
    fun `should get all tasks for project successfully`() = runTest {
        // given
        val task = createTaskHelper()
        val taskDocument = task.toDocument()
        val projectId = UUID.randomUUID()
        val filter = Document("projectId", projectId.toString())

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns taskDocument

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find(filter) } returns findIterableMock

        // when
        val result = taskMongoDataSource.getAllTasksForProject(projectId)

        // then
        assertEquals(1, result.size)
        assertEquals(task.id, result[0].id)
        verify(exactly = 1) { tasksCollection.find(filter) }
    }

    @Test
    fun `should throw TaskNotFoundException when getAllTasksForProject finds null document`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        val filter = Document("projectId", projectId.toString())

        val cursorMock = mockk<MongoCursor<Document>>()
        every { cursorMock.hasNext() } returns true andThen false
        every { cursorMock.next() } returns null

        val findIterableMock = mockk<FindIterable<Document>>()
        every { findIterableMock.iterator() } returns cursorMock

        every { tasksCollection.find(filter) } returns findIterableMock

        // when/then
        assertThrows<TaskNotFoundException> {
            runBlocking { taskMongoDataSource.getAllTasksForProject(projectId) }
        }
    }
}