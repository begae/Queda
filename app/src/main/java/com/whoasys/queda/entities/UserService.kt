package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object UserService {

    private const val SERVER = "http://118.67.129.26:8080/user/"

    @Throws(IOException::class)
    @JvmStatic
    fun call(): Http {

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(Http::class.java)
    }

    interface Http {

        @GET("id")
        fun findUserById(
            @Query("id") id: String
        ): Call<User?>

        @GET("with")
        fun findUserByStoreId(
            @Query("storeId") storeId: Int
        ): Call<User?>

        @POST("join")
        fun join(
            @Body user: User
        ): Call<User?>

        @GET("login")
        fun login(
            @Query("id") id: String,
            @Query("pw") pw: String
        ): Call<User?>

        @FormUrlEncoded
        @POST("manager")
        fun updateUserAsManager(
            @Field("userId") userId: String,
            @Field("storeId") storeId: Int
        ): Call<Boolean?>
    }
}