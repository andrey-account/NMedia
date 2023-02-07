package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import ru.netology.nmedia.databinding.ActivityMainBinding //Сгенерированный автоматически java класс
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val editPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }


        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }

            override fun onPlay(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onLook(post: Post) {
                viewModel.look(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                //.apply позволяет обращаться к объекту по его свойствам, не указывая вначале имя.
                val intent = Intent().apply { //Вызываем конструктор класса Intent
                    action = Intent.ACTION_SEND //Заполняем данными
                    putExtra(
                        Intent.EXTRA_TEXT,
                        post.content
                    ) //Кладём данные внутрь Intent(ключ и данные)
                    type = "text/plain" //MIME-тип данных
                }
                val shareIntent = Intent.createChooser(
                    intent,
                    getString(R.string.chooser_share_post)
                )//Удобное окно выбора репоста
                startActivity(shareIntent) //Запуск Activity
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) { //Добавляет элементы в RecyclerView
                if (newPost) {
                    binding.list.smoothScrollToPosition(0) //Переход к новому добавленному посту
                }
            }
        }

        binding.fab.setOnClickListener {
            editPostLauncher.launch(null) // Убрать null?
        }
    }
}