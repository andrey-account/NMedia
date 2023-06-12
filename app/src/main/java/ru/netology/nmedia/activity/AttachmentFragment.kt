package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.FragmentAttachmentBinding
import ru.netology.nmedia.util.StringArg

class AttachmentFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAttachmentBinding.inflate( //Создание экземпляра Binding класса для данного фрагмента
            inflater,
            container,
            false
        )
        val url = arguments?.textArg
        val attachmentUrlNew = "${BuildConfig.BASE_URL}/media/${url}"
        Glide.with(binding.attachmentFullscreen)
            .load(attachmentUrlNew) //Загружаем изображение с помощью библиотеки Glide и отображаем его в ImageView с идентификатором attachmentFullscreen
            .timeout(10_000)
            .into(binding.attachmentFullscreen)

        return binding.root
    }
}