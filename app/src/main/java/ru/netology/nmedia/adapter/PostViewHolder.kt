package ru.netology.nmedia.adapter

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.likeText

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (post.video != "") videoPreview.visibility = View.VISIBLE
            else videoPreview.visibility = View.GONE
            videoPreview.setOnClickListener {
                onInteractionListener.onPlay(post)
            }

            like.isChecked = post.likedByMe
            like.text = likeText(post.likes)
            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.text = likeText(post.shareClickCount)
            share.setOnClickListener {
                onInteractionListener.onShare(post) //with(onShareListener){onShare(post)}
            }

            look.setOnClickListener {
                onInteractionListener.onLook(post) //onLookListener(post)
            }
            lookTextView.text = likeText(post.lookClickCount)

            content.setOnClickListener { //При нажатии на текст поста должен произойти переход
                onInteractionListener.onPostFragment(post) //на фрагмент этого поста
            }
            published.setOnClickListener { //При нажатии на время публикации поста должен произойти
                onInteractionListener.onPostFragment(post) //переход на фрагмент этого поста
            }
            avatar.setOnClickListener {  //При нажатии на аватар поста должен произойти
                onInteractionListener.onPostFragment(post) //переход на фрагмент этого поста
            }
        }
    }
}