package data.csv
object  FileName{
    internal const val STATES = "states.csv"
    internal const val PROJECTS = "projects.csv"
    internal const val TASKS = "tasks.csv"
    internal const val PROJECT_LOG = "project_logs.csv"
    internal const val TASK_LOG = "task_logs.csv"
    internal const val REGISTERED_USERS_FILE = "registered_users.csv"
    internal const val CURRENT_USER_FILE = "current_user.csv"

    val allFiles = listOf(STATES, PROJECTS, TASKS, PROJECT_LOG, TASK_LOG, REGISTERED_USERS_FILE, CURRENT_USER_FILE)
}