package hr.algebra.surfsafely.viewmodel

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import hr.algebra.surfsafely.framework.toBitmapWithId
import hr.algebra.surfsafely.framework.toBitmapListWithId
import hr.algebra.surfsafely.model.ProfileAvatar
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
class UserViewModel(
    private val apiService: ApiService,
    private val tokenViewModel: TokenViewModel
) : ViewModel() {

    val userObserve = ObservableField<UserDto>()
    val pointsObserve = ObservableField<Long>()
    val moneyObserve = ObservableField<Long>()

    private val _user = MutableLiveData<UserDto?>()
    val user: LiveData<UserDto?> = _user

    private val _avatars = MutableLiveData<MutableMap<Long?, Bitmap?>>()
    val avatars: MutableLiveData<MutableMap<Long?, Bitmap?>> = _avatars

    private val _profileAvatar = MutableLiveData<ProfileAvatar>()
    val profileAvatar: LiveData<ProfileAvatar> = _profileAvatar

    private val _points = MutableLiveData<Long?>()
    val points: LiveData<Long?> = _points

    private val _money = MutableLiveData<Long?>()
    val money: LiveData<Long?> = _money

    init {
        viewModelScope.launch {
            getUserAvatars()
            getUserProfileAvatar()
            getUserPoints()
        }
    }

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
                    } else
                        Result.failure(Exception("User not found"))
                } else
                    Result.failure(Exception("Failed to get user"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.changePassword(changePasswordRequest).execute()
                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(Exception("Failed to change password"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun updateUserInformation(updateInformationDto: UserUpdateInformationDto): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.updateUserPersonalInformation(updateInformationDto).execute()
                if (response.isSuccessful) {
                    getUser()
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to update user information"))
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
                } else
                    Result.failure(Exception("Failed to logout"))
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
                } else
                    Result.failure(Exception("Failed to delete account"))
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

    suspend fun getUserAvatars(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllUsersAvatars().execute()
                if (response.isSuccessful) {
                    val images = response.body()?.data
                    _avatars.postValue(images?.toBitmapListWithId())
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to find user avatars"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    private suspend fun getUserProfileAvatar(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProfileAvatar().execute()
                if (response.isSuccessful) {
                    val image = response.body()?.data
                    _profileAvatar.postValue(image?.toBitmapWithId())
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to find user profile avatar"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun updateProfileAvatar(id: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.setProfileAvatar(id).execute()
                if (response.isSuccessful) {
                    getUserProfileAvatar()
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to update your profile avatar"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun getUserPoints(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserPoints().execute()
                if (response.isSuccessful) {
                    val score = response.body()?.data?.score
                    val money = response.body()?.data?.money
                    _points.postValue(score)
                    pointsObserve.set(score)
                    _money.postValue(money)
                    moneyObserve.set(money)
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to get your points and money"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

}