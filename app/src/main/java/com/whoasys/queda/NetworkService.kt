package com.whoasys.queda

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

object NetworkService {

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

        @POST("/api/post/new")
        fun savePost(
            @Body post: Post
        ): Call<Int?>

        @FormUrlEncoded
        @POST("/api/post/attach")
        fun attachURLs(
            @Field("id") id: Int,
            @Field("urls") urls: String
        ): Call<Int?>

        @GET("/api/post/{id}")
        fun getOnePost(
            @Path("id") id: String
        ): Call<Post?>

        @GET("/api/store/{id}")
        fun getOneStore(
            @Path("id") id: String
        ): Call<Store?>

        @GET("/api/post/by/{author}")
        fun getAllPostsBy(
            @Path("author") author: String
        ): Call<Iterable<Post>?>

        @POST("/api/user/join")
        fun join(
            @Body user: User
        ): Call<User?>

        @GET("/api/user/login")
        fun login(
            @Query("id") id: String,
            @Query("pw") pw: String
        ): Call<User?>

        @GET("/api/user/find")
        fun find(
            @Query("id") id: String
        ): Call<User?>

        @POST("/api/store/register")
        fun register(
            @Body store: Store
        ): Call<Int?>

        @FormUrlEncoded
        @POST("/api/user/manager")
        fun updateUserAsManager(
            @Field("userId") userId: String,
            @Field("storeId") storeId: Int
        ): Call<Boolean?>
    }
}