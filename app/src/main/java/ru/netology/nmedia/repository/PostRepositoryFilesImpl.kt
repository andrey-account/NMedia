package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.util.*

class PostRepositoryFilesImpl(private val context: Context) : PostRepository {
    private val gson = Gson()
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) { // если файл есть - читаем
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
                nextId = (posts.maxOfOrNull { post -> post.id } ?: 0) + 1
                //data.value = posts
            }
        } else {
            posts = listOf(
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    likedByMe = false
                ),
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    likedByMe = false
                ),
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    likedByMe = false
                ),
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    likedByMe = false
                ),
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    likedByMe = false
                ),
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "18 января в 18:27",
                    video = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
                    likedByMe = false
                ),
            ).reversed()
        }
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data


    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    private val author = "Андрей"
    override fun save(post: Post) {
        if (post.content != ""){//post.id == 0L Изменил условие, потому что при создании поста создавался ещё один пустой
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = author, //"Me",
                    likedByMe = false,
                    published = (getCurrentDateTime()).toString() //"now"
                )
            ) + posts
            data.value = posts
            sync()
            return
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
        sync()
    }


    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map{
            if (it.id != id) it else it.copy(shareClickCount = it.shareClickCount + 1)
        }
        data.value = posts
        sync()
    }

    override fun look(id: Long) {
        posts = posts.map{
            if (it.id != id) it else it.copy(lookClickCount = it.lookClickCount + 1)
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}