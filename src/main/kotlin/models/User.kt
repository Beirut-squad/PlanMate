package org.example.models

data class User(
    val id:String,
    val name:String,
    val password:String,
    val role:Role
)

enum class Role{
    ADMIN,MATE
}
