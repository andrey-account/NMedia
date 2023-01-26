package ru.netology.nmedia.dto

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnLikeListener
import ru.netology.nmedia.adapter.OnLookListener
import ru.netology.nmedia.adapter.OnShareListener
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.likeText

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onLookListener: OnLookListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) R.drawable.red_heart else R.drawable.white_heart
            )
            like.setOnClickListener {
                onLikeListener(post) //Вызов функции из класса PostViewModel
            }
            likeTextView.text = likeText(post.likeClickCount)//post.likeClickCount.toString()

            share.setOnClickListener {
                onShareListener(post)
                //viewModel.share(post.id) //Старый рабочий код
            }
            shareTextView.text = likeText(post.shareClickCount)

            look.setOnClickListener {
                onLookListener(post)
            }
            lookTextView.text = likeText(post.lookClickCount)
        }
    }
}