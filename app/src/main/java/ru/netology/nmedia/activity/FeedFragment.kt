package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id, post)
            }

            override fun onRemove(post: Post) {
                PostViewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                //.apply позволяет обращаться к объекту по его свойствам, не указывая вначале имя.
                val intent = Intent().apply { //Вызываем конструктор класса Intent
                    action = Intent.ACTION_SEND //Заполняем данными
                    putExtra(Intent.EXTRA_TEXT, post.content) //Кладём данные внутрь Intent(ключ и данные)
                    type = "text/plain" //MIME-тип данных
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post)) //Удобное окно выбора репоста
                startActivity(shareIntent) //Запуск Activity
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadPosts()
            binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }
}
