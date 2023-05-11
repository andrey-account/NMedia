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
    val data: LiveData<FeedModel>
        get() = _data
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true) //Устанавливает значение data.value в FeedModel(loading = true), будет показано состояние загрузки, пока данные постов загружаются.
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {//функция вызывает repository.getAllAsync() для получения списка всех постов из репозитория
            override fun onSuccess(value: List<Post>) {//onSuccess вызывается в случае успешного получения данных
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))//Внутри этого коллбэка _data получает новое значение через postValue(). Значение состоит из FeedModel с двумя параметрами: posts (список полученных постов) и empty (логическая переменная, показывающая, является ли список постов пустым).
            }
            override fun onError(e: Exception) {//Этот коллбэк вызывается, когда происходит ошибка при загрузке данных.
                _data.postValue(FeedModel(error = true))//Внутри этого коллбэка _data получает новое значение через postValue(). Значение состоит из FeedModel с параметром error равным true, что указывает на возникновение ошибки.
            }
        })//функция loadPosts() отвечает за загрузку данных постов из репозитория, меняет состояния _data для отображения процесса загрузки и остальных сценариев в зависимости от успешного получения данных или возникновения ошибки.
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(value: Post) {
                    _postCreated.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun removeById(id: Long) {//Функция принимает id типа Long в качестве аргумента, чтобы указать уникальный идентификатор элемента, который нужно удалить.
        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {//Обращается к репозиторию repository, вызывая метод removeByIdAsync(id, callback), который пытается удалить элемент с указанным идентификатором асинхронно.
            val old = _data.value?.posts.orEmpty()
            override fun onSuccess(value: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }//Фильтрация списка существующих элементов, исключая элемент с удаленным 'id' через использование функции filter { it.id != id }.
                    )
                )
            }
            override fun onError(e: Exception) {//На случай возникновения ошибки при удалении элемента
                _data.postValue(FeedModel(error = true)) //Установка FeedModel с параметром error = true для _data.
                _data.postValue(_data.value?.copy(posts = old))//Восстановление _data к предыдущему состоянию (когда до вызова removeById) используя old.
            }
        })
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        repository.likeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(
                    _data.value?.copy(
                        posts = _data.value?.posts.orEmpty()
                            .map {
                                if (it.id == id) value else it
                            }
                    )
                )
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun unlikeById(id: Long) {
        repository.unlikeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(
                    _data.value?.copy(
                        posts = _data.value?.posts.orEmpty()
                            .map {
                                if (it.id == id) value else it
                            }
                    )
                )
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
}