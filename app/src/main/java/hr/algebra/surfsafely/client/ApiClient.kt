package hr.algebra.surfsafely.client

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.algebra.surfsafely.application.SurfSafelyApplication.Companion.getApplication
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.service.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = runBlocking { TokenManager.getToken(getApplication() ?: throw Exception("Application context is null")) }
            val newRequest = chain.request().newBuilder()

            if (token != null) newRequest.addHeader("Authorization", "Bearer $token")

            chain.proceed(newRequest.build())
        }
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun getService(): ApiService = retrofit.create(ApiService::class.java)
}