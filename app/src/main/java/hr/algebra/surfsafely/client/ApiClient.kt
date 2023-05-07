package hr.algebra.surfsafely.client

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.algebra.surfsafely.manager.TokenManager
import hr.algebra.surfsafely.service.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {

    private const val BASE_URL = "http://192.168.5.15:8080/api/"
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun getAuthClient(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun getClient(): ApiService {

        val customInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequestBuilder = originalRequest.newBuilder()
            newRequestBuilder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2siLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE2ODM0MDEyMTQsImV4cCI6MTY4MzQ4NzYxNH0.GTqcdXW2QRC7dR9zGWbj5Rhdx-E9xPcpHk-c9eMg2YU")
            val newRequest = newRequestBuilder.build()
            chain.proceed(newRequest)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(customInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}