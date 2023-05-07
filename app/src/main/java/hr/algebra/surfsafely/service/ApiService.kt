package hr.algebra.surfsafely.service

import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.UserUpdateInformationDto
import hr.algebra.surfsafely.dto.checkfile.CheckFileResponse
import hr.algebra.surfsafely.dto.checkurl.CheckUrlRequest
import hr.algebra.surfsafely.dto.checkurl.CheckUrlResponse
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("checkUrl")
    fun checkUrl(@Body request: CheckUrlRequest): Call<ApiResponse<CheckUrlResponse, String>>

    @POST("checkFile")
    fun checkFile(@Part file: MultipartBody.Part): Call<ApiResponse<CheckFileResponse, String>>

    @GET("logout")
    fun logout(@Header("Authorization") token: String): Call<ApiResponse<String, String>>

    @POST("change-password")
    fun changePassword(@Body request: ChangePasswordRequest): Call<ApiResponse<String, String>>

    @POST("current-user/update-personal-information")
    fun updateUserPersonalInformation(@Body request: UserUpdateInformationDto): Call<ApiResponse<UserUpdateInformationDto, String>>
}