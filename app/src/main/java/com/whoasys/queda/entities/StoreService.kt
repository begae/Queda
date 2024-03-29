package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object StoreService {

    private const val SERVER = "http://118.67.129.26:8080/store/"

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
        fun getOneStore(
            @Query("id") id: Int
        ): Call<Store?>

        @POST("register")
        fun register(
            @Body store: Store
        ): Call<Int?>

        @PUT("update/{id}")
        fun updateStore(
            @Path("id") id: Int,
            @Body storeUpdate: Store
        ): Call<Response<Any>>

        @GET("temp")
        fun temporary(): Call<List<Store>?>

    }
}