package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object PostService {

    private const val SERVER = "http://118.67.129.26:8080/post/"

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
        fun savePost(
            @Body post: Post
        ): Call<Int?>

        @FormUrlEncoded
        @POST("attach")
        fun attach(
            @Field("num") num: Int,
            @Field("id") id: Int,
            @Field("key") key: String
        ): Call<Void>

        @GET("id")
        fun getOnePost(
            @Query("id") id: Int
        ): Call<Post?>

        @GET("nearby")
        fun getAllPostsNearby(
            @Query("userId") userId: String
        ): Call<List<Post>?>

        @GET("by")
        fun getAllPostsBy(
            @Query("author") author: String
        ): Call<List<Post>?>

        @PUT("/update/{id}")
        fun updatePost(
            @Path("id") id: Int,
            @Body post: Post
        ): Call<Response<String>>

        @DELETE("/delete/{id}")
        fun deletePost(
            @Path("id") id: Int
        ): Call<Response<String>>

    }
}