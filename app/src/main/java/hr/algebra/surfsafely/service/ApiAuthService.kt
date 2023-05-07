package hr.algebra.surfsafely.service

import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.UserDto
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthService {
    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse<LoginResponse, String>>

    @POST("auth/register")
    fun registerUser(@Body request: UserDto): Call<ApiResponse<UserDto, String>>
}