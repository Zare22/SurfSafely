package hr.algebra.surfsafely.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationUserViewModel(private val apiService: ApiService, private val tokenViewModel: TokenViewModel) : ViewModel() {

    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val username = ObservableField<String>()
    val password = ObservableField<String>()
    val email = ObservableField<String>()

    suspend fun registerUser(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val userDto = UserDto(
                firstName.get()!!,
                lastName.get()!!,
                username.get()!!,
                password.get()!!,
                email.get()!!
            )
            try {
                val response = apiService.registerUser(userDto).execute()
                if(response.isSuccessful) {
                    Result.success(Unit)
                }
                else
                    Result.failure(Exception("Registration failed!"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun loginUser(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val loginDto = LoginRequest(username.get()!!, password.get()!!)
            try {
                val response = apiService.loginUser(loginDto).execute()
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    if (token != null) tokenViewModel.updateToken(token)
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to log you in!"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error"))
            }
        }
    }
}