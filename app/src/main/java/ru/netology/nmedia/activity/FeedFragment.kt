package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    //val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { //Проверка на null не нужна!
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id, post)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onLook(post: Post) {
                viewModel.look(post.id)
            }


            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                //.apply позволяет обращаться к объекту по его свойствам, не указывая вначале имя.
                val intent = Intent().apply { //Вызываем конструктор класса Intent
                    action = Intent.ACTION_SEND //Заполняем данными
                    putExtra(
                        Intent.EXTRA_TEXT, post.content) //Кладём данные внутрь Intent(ключ и данные)
                    type = "text/plain" //MIME-тип данных
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))//Удобное окно выбора репоста
                startActivity(shareIntent) //Запуск Activity
            }

            override fun onPlay(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                val playIntent = Intent.createChooser(intent, getString(R.string.video))
                startActivity(intent)
            }

            override fun onPostFragment(post: Post) { //Переопределение функции для перехода
                findNavController().navigate(R.id.action_feedFragment_to_postFragment,
                Bundle().apply {idArg = post.id}) //на фрагмент отдельного поста
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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.fab.setOnClickListener { //Обработчик нажатия кнопки добавления постов
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg by LongArg
    }
}