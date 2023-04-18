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

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = "",
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
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
        _data.postValue(FeedModel(loading = true))
        repository.getAll(object : PostRepository.PostCallback<List<Post>> {
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let {
            if (it !== empty) {
                repository.save(it, object : PostRepository.PostCallback<Post> {
                    override fun onSuccess(value: Post) {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        _postCreated.postValue(Unit)
                        _data.postValue(FeedModel(error = true))
                    }
                })
            } else {
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post //Присваивается теущий отредактированный пост
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(id: Long, post: Post) {
        repository.likeById(post, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(value: Post) {
                val posts = _data.value?.posts.orEmpty().map {
                    if (it.id == id) {
                        value
                    } else {
                        it
                    }
                }
                _data.postValue(_data.value?.copy(posts = posts))
            }

            override fun onError(e: Exception) {
                println(e.message.toString())
            }
        })
    }



    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()// Оптимистичная модель
        repository.removeById(id, object : PostRepository.PostCallback<Unit> {
            override fun onSuccess(value: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }
}

