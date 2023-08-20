package ru.netology.nmedia.api

import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.model.AuthModel

interface ApiService { //Подогнал под сервер

// Auth
// Отправляет POST-запрос на сохранение токена устройства
@POST("users/push-tokens")
suspend fun saveToken(@Body pushToken: PushToken)

    // Отправляет POST-запрос на аутентификацию пользователя
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Response<AuthModel>

    // Отправляет POST-запрос на регистрацию пользователя без фотографии
    @FormUrlEncoded
    @POST("users/registration")
    suspend fun register(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Response<AuthModel>

    // Отправляет POST-запрос на регистрацию пользователя с фотографией
    @Multipart
    @POST("users/registration")
    suspend fun registerWithPhoto(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<AuthModel>


    // Posts

    // Отправляет GET-запрос для получения последних постов с указанным количеством
    @GET("posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<Post>>

    // Отправляет GET-запрос для получения постов, которые были опубликованы до указанного идентификатора с указанным количеством
    @GET("posts/{id}/before")
    suspend fun getPostsBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>
    // Отправляет GET-запрос для получения постов, которые были опубликованы после указанного идентификатора с указанным количеством
    @GET("posts/{id}/after")
    suspend fun getPostsAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    // Отправляет POST-запрос для создания нового поста
    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    // Отправляет DELETE-запрос для удаления поста с указанным идентификатором
    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Long): Response<Unit>

    // Отправляет POST-запрос для поставки лайка посту с указанным идентификатором
    @POST("posts/{id}/likes")
    suspend fun likePostById(@Path("id") postId: Long): Response<Post>

    // Отправляет DELETE-запрос для удаления лайка у поста с указанным идентификатором
    @DELETE("posts/{id}/likes")
    suspend fun dislikePostById(@Path("id") postId: Long): Response<Post>


    // Events

    // Отправляет GET-запрос для получения последних событий с указанным количеством
    @GET("events/latest")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<Post>>

    // Отправляет GET-запрос для получения событий, которые произошли до указанного идентификатора с указанным количеством
    @GET("events/{id}/before")
    suspend fun getEventsBefore(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

    // Отправляет GET-запрос для получения событий, которые произошли после указанного идентификатора с указанным количеством
    @GET("events/{id}/after")
    suspend fun getEventsAfter(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Post>>
    // Отправляет POST-запрос для создания нового события
    @POST("events")
    suspend fun createEvent(@Body event: Post): Response<Post>

    // Отправляет DELETE-запрос для удаления события с указанным идентификатором
    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int): Response<Unit>

    // Отправляет POST-запрос для поставки лайка событию с указанным идентификатором
    @POST("events/{id}/likes")
    suspend fun likeEventById(@Path("id") eventId: Int): Response<Post>

    // Отправляет DELETE-запрос для удаления лайка у события с указанным идентификатором
    @DELETE("events/{id}/likes")
    suspend fun dislikeEventById(@Path("id") eventId: Int): Response<Post>


    // Media

    // Отправляет POST-запрос для загрузки медиа-файла на сервер
    @Multipart
    @POST("media")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>


    //User

    // Отправляет GET-запрос для получения пользователя по указанному идентификатору
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>


    //Jobs

    // Отправляет GET-запрос для получения списка работ, связанных с пользователем по указанному идентификатору
    @GET("{id}/jobs")
    suspend fun getJobsByUserId(@Path("id") id: Int): Response<List<Job>>

    // Отправляет POST-запрос для сохранения работы
    @POST("my/jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    // Отправляет DELETE-запрос для удаления работы по указанному идентификатору
    @DELETE("my/jobs/{id}")
    suspend fun removeJobById(@Path("id") id: Int): Response<Unit>


    //Wall

    // Отправляет GET-запрос для получения последних постов на стене автора с указанным идентификатором и количеством
    @GET("{authorId}/wall/latest")
    suspend fun wallGetLatest(
        @Path("authorId") authorId: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    // Отправляет GET-запрос для получения постов на стене автора с указанным идентификатором и количеством, которые были опубликованы до указанного идентификатора поста
    @GET("{authorId}/wall/{postId}/before")
    suspend fun wallGetBefore(
        @Path("authorId") authorId: Long,
        @Path("postId") postId: Int,
        @Query("count") count: Int
    ): Response<List<Post>>
}