package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object KeywordService {

    private const val SERVER = "http://118.67.129.26:8080/keyword/"

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

        @POST("new")
        fun saveKeyword(
            @Body keyword: Keyword
        ): Call<Int?>

        @GET("all")
        fun getAllKeywords(): Call<List<Keyword>?>

        @GET("of")
        fun getAllKeywordsOf(
            @Query("userId") userId: String
        ): Call<List<Keyword>?>

    }
}