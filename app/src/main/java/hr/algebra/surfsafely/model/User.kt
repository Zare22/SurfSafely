package hr.algebra.surfsafely.model

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val password: String,
    val email: String,
    val role: Role
)
