package hr.algebra.surfsafely.viewmodel

import androidx.lifecycle.ViewModel
import hr.algebra.surfsafely.client.ApiClient

class AuthenticationViewModel : ViewModel() { val apiService = ApiClient.getService() }