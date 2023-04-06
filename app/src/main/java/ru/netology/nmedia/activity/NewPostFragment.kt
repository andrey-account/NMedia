package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let {
            binding.content.setText(it)
        }

        val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)

        binding.ok.setOnClickListener {
            val text = binding.content.text.toString() //Преобразование в строку всего, что в текстовом поле "content"
            viewModel.changeContentAndSave(text) //Сохранение текста
            viewModel.save() //Вызовет _postCreated.postValue(Unit)
            AndroidUtils.hideKeyboard(requireView()) //Убирает клавиатуру
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }
        return binding.root
    }


    companion object {
        var Bundle.textArg by StringArg
    }
}