package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemLoadingBinding

class PostLoadingStateAdapter(
    private val retryListener: () -> Unit,
) : LoadStateAdapter<PostLoadingViewHolder>() {
    //В методе onBindViewHolder() происходит привязка данных элемента списка к представлению.
    override fun onBindViewHolder(holder: PostLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState) //Отображает данные в RecyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, //Принимаются данные для создания нового элемента списка
        loadState: LoadState
    ): PostLoadingViewHolder = //Создается новый объект типа PostLoadingViewHolder,
        PostLoadingViewHolder(  //он используется для хранения данных элемента списка.
            ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            retryListener,
        )
}

class PostLoadingViewHolder(
    private val itemLoadingBinding: ItemLoadingBinding,
    private val retryListener: () -> Unit,
) : RecyclerView.ViewHolder(itemLoadingBinding.root) {
    fun bind(loadState: LoadState) {
        itemLoadingBinding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            retryButton.setOnClickListener {
                retryListener()
            }
        }
    }
}