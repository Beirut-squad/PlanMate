package data.csv
object  FileName{
    private const val STATES = "states.csv"
    private const val PROJECTS = "projects.csv"
    private const val TASKS = "tasks.csv"
    private const val PROJECT_LOG = "project_logs.csv"
    private const val TASK_LOG = "task_logs.csv"
    val allFiles = listOf(STATES, PROJECTS, TASKS, PROJECT_LOG, TASK_LOG)
}