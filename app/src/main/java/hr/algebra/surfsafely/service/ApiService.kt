package hr.algebra.surfsafely.service

import hr.algebra.surfsafely.dto.ApiResponse
import hr.algebra.surfsafely.dto.checkfile.CheckFileResponse
import hr.algebra.surfsafely.dto.checkurl.CheckUrlRequest
import hr.algebra.surfsafely.dto.checkurl.CheckUrlResponse
import hr.algebra.surfsafely.dto.login.LoginRequest
import hr.algebra.surfsafely.dto.login.LoginResponse
import hr.algebra.surfsafely.dto.password.ChangePasswordRequest
import hr.algebra.surfsafely.dto.quiz.QuizDto
import hr.algebra.surfsafely.dto.quiz.SolveAttemptDto
import hr.algebra.surfsafely.dto.quiz.SolveAttemptResultDto
import hr.algebra.surfsafely.dto.user.UserDto
import hr.algebra.surfsafely.dto.user.AvatarDto
import hr.algebra.surfsafely.dto.user.UserPointsDto
import hr.algebra.surfsafely.dto.user.UserUpdateInformationDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse<LoginResponse, String>>

    @POST("auth/register")
    fun registerUser(@Body request: UserDto): Call<ApiResponse<UserDto, String>>

    @POST("checkUrl")
    fun checkUrl(@Body request: CheckUrlRequest): Call<ApiResponse<CheckUrlResponse, String>>

    @Multipart
    @POST("checkFile")
    fun checkFile(@Part file: MultipartBody.Part): Call<ApiResponse<CheckFileResponse, String>>

    @GET("auth/logout")
    fun logout(): Call<ApiResponse<String, String>>

    @POST("auth/change-password")
    fun changePassword(@Body request: ChangePasswordRequest): Call<ApiResponse<String, String>>

    @POST("current-user/update-personal-information")
    fun updateUserPersonalInformation(@Body request: UserUpdateInformationDto): Call<ApiResponse<UserDto, String>>

    @DELETE("current-user/delete-account")
    fun deleteAccount(): Call<ApiResponse<String, String>>

    @GET("current-user")
    fun getCurrentUser(): Call<ApiResponse<UserDto, String>>

    @POST("quiz/create")
    fun createQuiz(@Body request: QuizDto) : Call<ApiResponse<QuizDto, String>>

    @DELETE("quiz/delete/{id}")
    fun deleteQuiz(@Path("id") id: Long) : Call<ApiResponse<String, String>>

    @DELETE("quiz/delete/all")
    fun deleteAllQuizzes() : Call<ApiResponse<String, String>>

    @GET("quiz/all")
    fun getAllQuizzes() : Call<ApiResponse<List<QuizDto>, String>>

    @GET("quiz/{id}")
    fun getQuiz(@Path("id") id: Long) : Call<ApiResponse<QuizDto, String>>

    @POST("quiz/solve")
    fun solveQuiz(@Body request: SolveAttemptDto) : Call<ApiResponse<SolveAttemptResultDto, String>>

    @GET("leaderboard")
    fun getLeaderboard() : Call<ApiResponse<List<UserPointsDto>, String>>

    @GET("leaderboard/user/{id}")
    fun getUsernameForLeaderboard(@Path("id") id: Long) : Call<ApiResponse<UserDto, String>>

    @GET("avatar/currentUser")
    fun getProfileAvatar() : Call<ApiResponse<AvatarDto, String>>

    @POST("avatar/makeMain/{avatarId}")
    fun setProfileAvatar(@Path("avatarId") id: Long) : Call<ApiResponse<String, String>>

    @GET("avatar/currentUser/all")
    fun getAllUsersAvatars() : Call<ApiResponse<List<AvatarDto>, String>>

    @GET("avatar/all")
    fun getAllAvatars() : Call<ApiResponse<List<AvatarDto>, String>>

    @POST("avatar/buy/{avatarId}")
    fun buyAvatar(@Path("avatarId") id: Long) : Call<ApiResponse<String, String>>

    @GET("current-user/get-points")
    fun getUserPoints() : Call<ApiResponse<UserPointsDto, String>>
}