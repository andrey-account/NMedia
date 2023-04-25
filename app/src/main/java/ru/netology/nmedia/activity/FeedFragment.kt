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
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
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
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: Post) {
                if (post.likedByMe) {
                    viewModel.unlikeById(post.id)
                } else {
                    viewModel.likeById(post.id)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) { //Функция принимает объект класса пост в качестве параметра
                val intent = Intent().apply {//Создаётся новый объект Intent, который будет использоваться для отправления текстовых данных.
                    action = Intent.ACTION_SEND //Присвоение действия ACTION_SEND добавленному объекту Intent
                    putExtra(Intent.EXTRA_TEXT, post.content)//Добавление текстовых данных (содержимого поста) внутрь объекта Intent
                    type = "text/plain" //Присвоение типа данных "text/plain" добавленному объекту Intent
                } /*В результате выполнения кода, объект Intent будет содержать текстовое сообщение из поста, которое можно отправить любым доступным для отправки способом на устройстве.
Объект Intent является своеобразным "контейнером" для передачи информации между компонентами операционной системы Android. В данном случае, используется его функционал для отправки сообщения. */

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })
        binding.list.adapter = adapter //Устанавливает адаптер для RecyclerView, который находится в binding (в данном случае, это объект ViewBinding).
        viewModel.data.observe(viewLifecycleOwner) { state -> //Ожидает изменения данных от viewModel (это может быть любой объект, который предоставляет данные для отображения) и выполняет блок кода внутри фигурных скобок, используя state (объект состояния) в качестве аргумента.
            adapter.submitList(state.posts) //Отправляет новый список данных state.posts в адаптер, что приводит к обновлению списка элементов в RecyclerView.
            binding.progress.isVisible = state.loading //Устанавливает видимость элемента ProgressBar (binding.progress) в соответствии со значением state.loading, которое может указывать, загружаются ли данные в данный момент или нет.
            binding.errorGroup.isVisible = state.error //Устанавливает видимость группы элементов binding.errorGroup в соответствии со значением state.error, которое может указывать на наличие ошибок в получении данных.
            binding.emptyText.isVisible = state.empty //Устанавливает видимость элемента TextView (binding.emptyText) в соответствии со значением state.empty, которое может указывать, пуст ли список данных или нет.
        }//Этот код изменяет содержимое RecyclerView в соответствии с изменениями данных, получаемыми из viewModel. Он также отображает элементы интерфейса, такие как ProgressBar, в зависимости от текущего состояния получения данных.

        binding.retryButton.setOnClickListener {//Кнопка переподключения (попробовать снова)
            viewModel.loadPosts()
        }

        binding.fab.setOnClickListener {//Плавающая кнопка для добавления нового сообщения
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener { //Обновление списка сообщений с помощью жеста pull-to-refresh
            viewModel.loadPosts()
            binding.swiperefresh.isRefreshing = false
        }


        viewModel.data.observe(viewLifecycleOwner) { FeedModel -> //С помощью метода observe() устанавливается наблюдатель за изменением данных модели, который вызывает функцию, когда изменения обнаруживаются
            if (FeedModel.error) { //Условие if должно проверять значение поля error на true
                //Если значение равно true, то выполняется отображение всплывающего сообщения Snack bar, которое информирует пользователя о том, что произошла ошибка при загрузке данных.
                Snackbar.make(requireView(), R.string.error_loading, Snackbar.LENGTH_LONG).show()
            }
        }
        return binding.root //Функция возвращает корневой View фрагмента
    }
}