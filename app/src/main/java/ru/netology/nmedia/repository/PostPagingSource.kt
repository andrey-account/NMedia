package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post
import java.io.IOException

class PostPagingSource(
    private val apiService: ApiService
) : PagingSource<Long, Post>() {
    //Функция возвращает ключ для обновления данных в случае, если пэйджер был обновлен.
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

//Функция "load" загружает данные для текущей страницы.
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
    //В зависимости от типа параметра "LoadParams", функция выполняет запрос на получение последних постов,
    //постов до определенного поста или постов после определенного поста.
        try {
            val result = when (params) {
                is LoadParams.Refresh -> {
                    apiService.getLatest(params.loadSize)
                }
                is LoadParams.Append -> {
                    apiService.getBefore(id = params.key, count = params.loadSize)
                }
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(), nextKey = null, prevKey = params.key,
                )
            }
            //Если запрос не был успешным (код ответа сервера не в диапазоне 200-299),
            // то выбрасывается исключение "HttpException".
            if (!result.isSuccessful) {
                throw HttpException(result)
            }
            val data = result.body().orEmpty()
            return LoadResult.Page(data = data, prevKey = params.key, data.lastOrNull()?.id)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}