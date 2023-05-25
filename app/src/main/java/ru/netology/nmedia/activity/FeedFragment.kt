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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels() //Определяется свойство viewModel, которое используется для получения экземпляра класса PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {//Создается объект binding, который используется для связывания макета фрагмента с кодом
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {//Адаптер используется для отображения списка постов в пользовательском интерфейсе
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            override fun onLike(post: Post) {
                if (post.likedByMe) { //Проверка значения свойства likedByMe объекта post
                    viewModel.unlikeById(post.id) //Удаляет лайк пользователя для данного поста
                } else { //Если свойство likedByMe равно false, то вызывается метод likeById
                    viewModel.likeById(post.id) //Добавляет лайк пользователя для данного поста
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
            override fun onAttachment(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_attachmentFragment,
                    Bundle().apply {
                        textArg = post.attachment?.url
                    }
                )
            }
        })
        binding.list.adapter = adapter //Устанавливает адаптер для RecyclerView, который находится в binding (в данном случае, это объект ViewBinding).

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() { //Регистрирует объект RecyclerView.AdapterDataObserver() в качестве слушателя изменений данных адаптера
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) { //Вызывается, когда в адаптере добавляются новые элементы в указанный диапазон позиций
                if (positionStart == 0) { //Проверка, были ли добавлены элементы в начало списка
                    binding.list.smoothScrollToPosition(0) //Плавно прокручивает список на самую верхнюю позицию
                }
            }
        })

        viewModel.data.observe(viewLifecycleOwner) { state -> //Ожидает изменения данных от viewModel (это может быть любой объект, который предоставляет данные для отображения) и выполняет блок кода внутри фигурных скобок, используя state (объект состояния) в качестве аргумента.
            adapter.submitList(state.posts) //Отправляет новый список данных state.posts в адаптер, что приводит к обновлению списка элементов в RecyclerView.
            binding.emptyText.isVisible = state.empty //Устанавливает видимость элемента TextView (binding.emptyText) в соответствии со значением state.empty, которое может указывать, пуст ли список данных или нет.
        }//Этот код изменяет содержимое RecyclerView в соответствии с изменениями данных, получаемыми из viewModel. Он также отображает элементы интерфейса, такие как ProgressBar, в зависимости от текущего состояния получения данных.

        viewModel.dataState.observe(viewLifecycleOwner) { state -> //отслеживает изменения объекта dataState и вызывает лямбда-выражение, переданное в качестве аргумента, когда происходят изменения в объекте.
            binding.progress.isVisible = state.loading //устанавливается видимость индикатора прогресса
            binding.swiperefresh.isRefreshing = state.refreshing //устанавливается видимость индикатора обновления swiperefresh
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_loading) {
                        viewModel.loadPosts()
                    }
                    .show()
            }
        }

        binding.fab.setOnClickListener {//Плавающая кнопка для добавления нового сообщения
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.newerCount.observe(viewLifecycleOwner) {
            println(it)
            if(it >= 1) {
                binding.getNewer.isVisible = true
                binding.getNewer.text = getString(R.string.newer_posts, it.toString())
                binding.getNewer.setOnClickListener {
                    viewModel.showNewPosts()
                    binding.getNewer.isVisible = false
                }
            }
        }


        binding.swiperefresh.setOnRefreshListener { //Обновление списка сообщений с помощью жеста pull-to-refresh
            viewModel.loadPosts()
            binding.swiperefresh.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it.message as CharSequence, Snackbar.LENGTH_LONG).show()
        }

        return binding.root //Функция возвращает корневой View фрагмента
    }
}