package data.csv
object  FileName{
    internal const val STATES = "states.csv"
    internal const val PROJECTS = "projects.csv"
    internal const val TASKS = "tasks.csv"
    internal const val PROJECT_LOG = "project_logs.csv"
    internal const val TASK_LOG = "task_logs.csv"

    val allFiles = listOf(STATES, PROJECTS, TASKS, PROJECT_LOG, TASK_LOG)
}