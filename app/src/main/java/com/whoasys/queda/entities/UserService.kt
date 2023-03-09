package com.whoasys.queda.entities

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object UserService {

    private const val SERVER = "http://118.67.129.26:8080"

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

        @GET("/store/id")
        fun getOneStore(
            @Query("id") id: Int
        ): Call<Store?>

        @GET("/user/id")
        fun findUserById(
            @Query("id") id: String
        ): Call<User?>

        @GET("/user/with")
        fun findUserByStoreId(
            @Query("storeId") storeId: Int
        ): Call<User?>

        @GET("/post/by")
        fun getAllPostsBy(
            @Query("author") author: String
        ): Call<Iterable<Post>?>

        @POST("/user/join")
        fun join(
            @Body user: User
        ): Call<User?>

        @GET("/user/login")
        fun login(
            @Query("id") id: String,
            @Query("pw") pw: String
        ): Call<User?>

        @POST("/store/register")
        fun register(
            @Body store: Store
        ): Call<Int?>

        @FormUrlEncoded
        @POST("/user/manager")
        fun updateUserAsManager(
            @Field("userId") userId: String,
            @Field("storeId") storeId: Int
        ): Call<Boolean?>
    }
}