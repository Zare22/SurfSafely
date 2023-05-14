package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.dto.user.RegisterUserDto

class RegisterUserViewModel : ViewModel() {
    private val _user = MutableLiveData<RegisterUserDto>()
    val user: LiveData<RegisterUserDto> = _user

    fun updateUser(firstName: String, lastName: String, username: String, password: String, email: String) {
        val updatedUser = RegisterUserDto(firstName, lastName, username, password, email)
        _user.value = updatedUser
    }
}