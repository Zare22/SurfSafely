package hr.algebra.surfsafely.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.framework.toAvatarItemListWithPrice
import hr.algebra.surfsafely.model.AvatarItem
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
class BuyAvatarViewModel(private val apiService: ApiService) : ViewModel() {

    private val _avatars = MutableLiveData<List<AvatarItem>>()
    val avatars: MutableLiveData<List<AvatarItem>> = _avatars

    init {
        viewModelScope.launch {
            getAllAvatars()
        }
    }

    suspend fun getAllAvatars(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllAvatars().execute()
                if (response.isSuccessful) {
                    val images = response.body()?.data
                    _avatars.postValue(images?.toAvatarItemListWithPrice())
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to find avatars"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    suspend fun buySelectedAvatar(id: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.buyAvatar(id).execute()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to buy selected avatar"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }
}