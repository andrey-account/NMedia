package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.FragmentAttachmentBinding
import ru.netology.nmedia.util.StringArg

@AndroidEntryPoint //Этот класс будет использоваться в качестве точки входа для Hilt
class AttachmentFragment : Fragment() {
    companion object { //Объявляем companion object, чтобы добавить расширение для Bundle
        var Bundle.textArg: String? by StringArg //чтобы получить значение из Bundle
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