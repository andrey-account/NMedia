package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.dto.*
import ru.netology.nmedia.model.AuthModel

interface ApiService {

    // Auth

    @POST("users/push-tokens")
    suspend fun saveToken(@Body pushToken: PushToken)

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<AuthModel>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun register(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Response<AuthModel>


    @Multipart
    @POST("users/registration")
    suspend fun registerWithPhoto(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<AuthModel>


    // Posts

    @GET("posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getPostsBefore(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getPostsAfter(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likePostById(@Path("id") postId: Int): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikePostById(@Path("id") postId: Int): Response<Post>


    // Events

    @GET("events/latest")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getEventsBefore(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getEventsAfter(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Event>>

    @POST("events")
    suspend fun createEvent(@Body event: Event): Response<Event>

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int): Response<Unit>

    @POST("events/{id}/likes")
    suspend fun likeEventById(@Path("id") eventId: Int): Response<Event>

    @DELETE("events/{id}/likes")
    suspend fun dislikeEventById(@Path("id") eventId: Int): Response<Event>


    // Media

    @Multipart
    @POST("media")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>


    //User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>


    //Jobs

    @GET("{id}/jobs")
    suspend fun getJobsByUserId(@Path("id") id: Int): Response<List<Job>>

    @POST("my/jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @DELETE("my/jobs/{id}")
    suspend fun removeJobById(@Path("id") id: Int): Response<Unit>


    //Wall

    @GET("{authorId}/wall/latest")
    suspend fun wallGetLatest(
        @Path("authorId") authorId: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{authorId}/wall/{postId}/before")
    suspend fun wallGetBefore(
        @Path("authorId") authorId: Int,
        @Path("postId") postId: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

}