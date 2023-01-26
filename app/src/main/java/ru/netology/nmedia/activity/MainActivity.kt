package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding //Сгенерированный автоматически java класс
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.likeText
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { posts ->
            binding.container.removeAllViews()
            posts.map { post ->
                CardPostBinding.inflate(layoutInflater, binding.container, true).apply {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content

                    like.setImageResource(
                        if (post.likedByMe) R.drawable.red_heart else R.drawable.white_heart
                    )
                    like.setOnClickListener {
                        viewModel.likeById(post.id)
                    }
                    likeTextView.text = likeText(post.likeClickCount)//post.likeClickCount.toString()


                    share.setOnClickListener {
                        viewModel.share(post.id)
                    }
                    shareTextView.text = likeText(post.shareClickCount)

                    look.setOnClickListener {
                        viewModel.look(post.id)
                    }
                    lookTextView.text = likeText(post.lookClickCount)
                }.root
            }
        }
    }
}





/*
//Код из презентации. Не работает.
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            val viewModel: PostViewModel by viewModels()
            val adapter = PostsAdapter {
                viewModel.likeById(it.id)
                viewModel.share(it.id)
                viewModel.look(it.id)
            }
            binding.list.adapter = adapter
            viewModel.data.observe(this) { posts ->
                adapter.submitList(posts)
            }
        }
    }
}
*/