package hr.algebra.surfsafely.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.dto.user.FullUserDto
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val apiService: ApiService) : ViewModel() {

    val userObserve = ObservableField<FullUserDto>()

    private val _user = MutableLiveData<FullUserDto>()
    val user: LiveData<FullUserDto> = _user

    fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = apiService.getCurrentUser().execute()
                _user.postValue(response.body()?.data!!)
                userObserve.set(response.body()?.data!!)
            }
        }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                apiService.changePassword(changePasswordRequest).execute()
            }
        }
    }

    fun updateUserInformation(updateInformationDto: UserUpdateInformationDto) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                apiService.updateUserPersonalInformation(updateInformationDto).execute()
            }
            getUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                apiService.logout().execute()
            }
        }
    }
}