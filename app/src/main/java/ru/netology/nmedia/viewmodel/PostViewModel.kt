package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0L,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0L,
    shares = 0L,
    video = "https://www.youtube.com/watch?v=O80turoMNgM"
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> //Хранит список постов
        get() = _data

    private val edited = MutableLiveData(empty) //Хранит текущий редактируемый элемент
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true)) // Начинаем загрузку
            try {
                val posts = repository.getAll() // Данные успешно получены
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true) // Получена ошибка
            }.also(_data::postValue)
        }
    }

    fun shareById(id: Long) = repository.shareById(id)

    fun look(id: Long) = repository.look(id) //Просмотры постов


    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) { edited.value = post } //Присваивается теущий отредактированный пост


    fun changeContentAndSave(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value?.let {
            thread { //Добавил thread, приложение перестало падать после добавления поста.
                repository.save(it.copy(content = text))
            }
        edited.value = empty
        }
    }

    fun likeById(id: Long, post: Post) {
        thread {
            try {
                val post = repository.likeById(post)
                val posts = _data.value?.posts.orEmpty().map {
                    if (it.id == id) {
                        post
                    } else {
                        it
                    }
                }
                _data.postValue(_data.value?.copy(posts = posts as List<Post>))
            } catch (e: IOException) {
                println(e.message.toString())
            }
        }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()// Оптимистичная модель
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }
}