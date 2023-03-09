package com.whoasys.queda.entities

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*
import java.io.IOException

object PostService {

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

        @POST("/post/new")
        fun savePost(
            @Body post: Post
        ): Call<Int?>

        @FormUrlEncoded
        @POST("/post/attach")
        fun attach(
            @Field("num") num: Int,
            @Field("id") id: Int,
            @Field("key") key: String
        ): Call<Int?>

        @GET("/post/id")
        fun getOnePost(
            @Query("id") id: Int
        ): Call<Post?>

        @GET("/post/by")
        fun getAllPostsBy(
            @Query("author") author: String
        ): Call<Iterable<Post>?>

    }
}