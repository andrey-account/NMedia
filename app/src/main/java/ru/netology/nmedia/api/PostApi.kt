package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

interface PostsApiService { //Интерфейс PostsApiService предоставляет несколько методов для выполнения операций с постами в удаленном API
    @GET("posts")//Аннотация указывает тип запроса (GET, POST, DELETE), а также URL-адрес конечной точки, к которой должен быть отправлен запрос
    suspend fun getAll(): Response<List<Post>> //Метод будет отправлять GET-запрос на URL-адрес "posts" в удаленном API

    @POST("posts")//методы возвращают объекты Response<List>, которые содержат информацию о статусе ответа и теле ответа.
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{postId}/likes")
    suspend fun likeById(@Path("postId") id: Long): Response<Post>

    @DELETE("posts/{postId}/likes")
    suspend fun unlikeById(@Path("postId") id: Long): Response<Post>
}

object PostsApi {

    private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    val retrofitService by lazy {
        retrofit.create<PostsApiService>()
    }
}