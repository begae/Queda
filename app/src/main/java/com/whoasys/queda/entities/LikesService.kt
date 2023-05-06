package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object LikesService {

    private const val SERVER = "http://118.67.129.26:8080/likes/"

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

        @FormUrlEncoded
        @POST("scrap")
        fun scrap(
            @Field("userId") userId: String,
            @Field("postId") postId: Int
        ): Call<Boolean?>

        @FormUrlEncoded
        @POST("follow")
        fun follow(
            @Field("userId") userId: String,
            @Field("storeId") storeId: Int
        ): Call<Boolean?>

        @FormUrlEncoded
        @POST("select")
        fun selectKeyword(
            @Field("userId") userId: String,
            @Field("keywordId") keywordId: Int
        ): Call<Boolean?>

        @GET("posts")
        fun getScraps(
            @Query("userId") userId: String
        ): Call<List<Post>?>

        @GET("stores")
        fun getFollowing(
            @Query("userId") userId: String
        ): Call<List<Store>?>

        @GET("keywords")
        fun getKeywords(
            @Query("userId") userId: String
        ): Call<List<Keyword>?>

    }
}