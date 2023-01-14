package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding //Сгенерированный автоматически java класс
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

var likeClickCount = 999 //Счётчик лайков
var shareClickCount = 1 //Счётчик репостов
var lookClickCount = 1 //Счётчик просмотров

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val post = Post(
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
            likedByMe = false
        )

        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.messageTextView)
        textView.movementMethod = LinkMovementMethod.getInstance()

        val printLikes: TextView = findViewById(R.id.heartTextView) //Вывод результата в TextView
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.likedByMe) {
                like.setImageResource(R.drawable.red_heart)
                printLikes.text = likeText(likeClickCount) //Выводит текст из фукции likeText(likeClickCount)
            }
        }

        like.setOnClickListener {
            if (it !is ImageButton) {
                return@setOnClickListener
            }
            if (!post.likedByMe) {
                likeClickCount++ //Увеличивает количество лайков +1
                like?.setImageResource(R.drawable.red_heart) //Меняет сердце на красное
                post.likedByMe = !post.likedByMe //Изменение состояние лайка (ВКЛ - ВЫКЛ)
                printLikes.text = likeText(likeClickCount) //Выводит текст из фукции likeText()
            } else {
                likeClickCount--
                like?.setImageResource(R.drawable.white_heart) //Меняет сердце на белое
                post.likedByMe = !post.likedByMe
                printLikes.text = likeText(likeClickCount)
            }
        }

        share.setOnClickListener {
            if (it !is ImageButton) {
                return@setOnClickListener
            }
            shareClickCount++
            val printShares: TextView = findViewById(R.id.shareTextView)
            printShares.text = likeText(shareClickCount)
        }

        look.setOnClickListener {
            if (it !is ImageButton) {
                return@setOnClickListener
            }
            lookClickCount++
            val printLooks: TextView = findViewById(R.id.lookTextView)
            printLooks.text = likeText(lookClickCount)
        }


        binding.root.setOnClickListener { //На root не получается поставить обработчик!
            Log.d("root", "root is clicked")
        }
        messageTextView.setOnClickListener{
            Log.d("text", "text is clicked")
        }
    }
}