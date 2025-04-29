package data.repositories.project_repository


import io.mockk.mockk
import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.project_data_source.ProjectDataSource
import org.example.data.repositories.project_repository.ProjectRepositoryImpl
import org.example.models.Project
import org.junit.jupiter.api.BeforeEach

class ProjectRepositoryImplTest {
    private var logDataSource: LogDataSource = mockk(relaxed = true)
    private var projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private var project: Project = mockk()

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(
            logDataSource = logDataSource,
            projectDataSource = projectDataSource
        )
    }
}