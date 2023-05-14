package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.dto.user.FullUserDto
import hr.algebra.surfsafely.dto.user.RegisterUserDto

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<FullUserDto>()
    val user: LiveData<FullUserDto> = _user

    fun setUser(user: FullUserDto) {
        _user.value = user
    }
}