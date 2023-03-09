package com.whoasys.queda.entities

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*
import java.io.IOException

object StoreService {

    private const val SERVER = "http://118.67.129.26:8080"

    @Throws(IOException::class)
    @JvmStatic
    fun call(): Http {

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return retrofit.create(Http::class.java)
    }

    interface Http {

        @GET("/store/id")
        fun getOneStore(
            @Query("id") id: Int
        ): Call<Store?>

        @POST("/store/register")
        fun register(
            @Body store: Store
        ): Call<Int?>
    }
}