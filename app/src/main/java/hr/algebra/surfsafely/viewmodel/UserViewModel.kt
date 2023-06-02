package hr.algebra.surfsafely.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(private val apiService: ApiService, private val tokenViewModel: TokenViewModel) : ViewModel() {

    val userObserve = ObservableField<UserDto>()

    private val _user = MutableLiveData<UserDto?>()
    val user: LiveData<UserDto?> = _user

    suspend fun getUser(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentUser().execute()
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    if (user != null) {
                        _user.postValue(user)
                        userObserve.set(user)
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("User not found"))
                    }
                } else {
                    Result.failure(Exception("Failed to get user"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.changePassword(changePasswordRequest).execute()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to change password"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun updateUserInformation(updateInformationDto: UserUpdateInformationDto): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateUserPersonalInformation(updateInformationDto).execute()
                if (response.isSuccessful) {
                    getUser()
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to update user information"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.logout().execute()
                if (response.isSuccessful) {
                    tokenViewModel.updateToken("")
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to logout"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun delete(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteAccount().execute()
                if (response.isSuccessful) {
                    tokenViewModel.updateToken("")
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to delete account"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun loginUser(password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val loginDto = LoginRequest(user.value?.username!!, password)
            try {
                val response = apiService.loginUser(loginDto).execute()
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    if (token != null) tokenViewModel.updateToken(token)
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to log in!"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }
}