package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.dto.PostViewHolder
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit //Тип для Callbаck`a
typealias OnShareListener = (post: Post) -> Unit // Для обработки нажатия кнопки share
typealias OnLookListener = (post: Post) -> Unit // Для обработки нажатия кнопки look

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onLookListener: OnLookListener
) : RecyclerView.Adapter<PostViewHolder>(){
    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged() //Уведомляет об изменении данных
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener, onLookListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size
}