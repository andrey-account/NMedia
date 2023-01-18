package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология\n" +
                "        начиналась с интенсивов по онлайн-маркетингу. Затем\n" +
                "        появились курсы по дизайну, разработке, аналитике\n" +
                "        и управлению. Мы растём сами и помогаем расти\n" +
                "        студентам: от новичков до уверенных профессионалов.\n" +
                "        Но самое важное остаётся с нами: мы верим, что в\n" +
                "        каждом уже есть сила, которая заставляет хотеть\n" +
                "        больше, целиться выше, бежать быстрее. Наша миссия\n" +
                "        - помочь встать на путь роста и начать цепочку\n" +
                "        перемен -> http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likeClickCount = 0,
        shareClickCount = 0,
        lookClickCount = 0
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data //returns data

    override fun like() {
        //Creates a copy of object post, reverses the value of post.likedByMe and increment or decrement post.likeClickCount
        post = post.copy(likedByMe = !post.likedByMe, likeClickCount = post.likeClickCount + if (post.likedByMe) -1 else 1)
        data.value = post //overwrites the updated object post
    }

    override fun share() {
        post = post.copy(shareClickCount = post.shareClickCount + 1)
        data.value = post
    }

    override fun look() {
        post = post.copy(lookClickCount = post.lookClickCount + 1)
        data.value = post
    }
}