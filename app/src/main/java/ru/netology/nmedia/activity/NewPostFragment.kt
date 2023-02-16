package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
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
            val text = binding.content.text.toString()
            viewModel.changeContentAndSave(text)
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object { //Объект, принадлежащий неявно всем экземплярам класса
        var Bundle.textArg by StringArg
    }
}