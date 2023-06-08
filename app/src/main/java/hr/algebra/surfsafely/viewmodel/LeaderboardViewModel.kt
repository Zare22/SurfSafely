package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.surfsafely.model.UserLeaderboard
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaderboardViewModel(private val apiService: ApiService) : ViewModel() {

    private val _userList = MutableLiveData<List<UserLeaderboard?>?>()
    val userList : LiveData<List<UserLeaderboard?>?> = _userList

    init {
        viewModelScope.launch {
            getUserLeaderboard()
        }
    }

    private suspend fun getUserLeaderboard() : Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLeaderboard().execute()
                if (response.isSuccessful) {
                    val userList = response.body()?.data
                    val userLeaderboardList = mutableListOf<UserLeaderboard>()
                    userList?.forEach { user ->
                        val username = getUsername(user.userId)
                        userLeaderboardList.add(UserLeaderboard(username.getOrNull(), user.score))
                    }
                    userLeaderboardList.sortByDescending { user ->
                        user.points
                    }
                    _userList.postValue(userLeaderboardList)
                    Result.success(Unit)
                } else
                    Result.failure(Exception("Failed to fetch leaderboard"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }

    private suspend fun getUsername(userId: Long) : Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUsernameForLeaderboard(userId).execute()
                if (response.isSuccessful) {
                    val username = response.body()?.data?.username
                    Result.success(username!!)
                } else
                    Result.failure(Exception("Failed to fetch username"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error!"))
            }
        }
    }
}