package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import ru.netology.nmedia.databinding.ActivityMainBinding //Сгенерированный автоматически java класс
import android.widget.TextView
import androidx.activity.viewModels
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                like.setImageResource(
                    if (post.likedByMe) R.drawable.red_heart else R.drawable.white_heart
                )
                likeTextView.text = likeText(post.likeClickCount)//post.likeClickCount.toString()

                shareTextView.text = likeText(post.shareClickCount)

                lookTextView.text = likeText(post.lookClickCount)
            }
        }
        binding.like.setOnClickListener{
            viewModel.like() //Вызов функции из класса PostViewModel
        }

        binding.share.setOnClickListener{
            viewModel.share()
        }

        binding.look.setOnClickListener{
            viewModel.look()
        }

        val textView: TextView = findViewById(R.id.content)
        textView.movementMethod = LinkMovementMethod.getInstance() //Для перехода по ссылке в тексте
    }
}