package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding

class IntentHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let //Указание имени функции, в которую эта лямбда передаётся (@let)
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT) //Получаем текст из Intent
            if (text.isNullOrBlank()) { //Проверка на пустоту
                //Показывать объявление "the content cannot be empty!" бесонечно долго
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { //Показывает строку ОК из ресурсов Андроид
                        finish() //Если текст пустой - завершаем работу Activity
                    }
                    .show() //Если текст не пустой - показываем
                return@let
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}