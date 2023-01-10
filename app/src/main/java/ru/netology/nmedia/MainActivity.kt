package ru.netology.nmedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.ImageButton
import android.widget.TextView

import kotlin.math.roundToInt

var heartClickCount = 1_099_998 //Счётчик лайков
var shareClickCount = 1_099_998 //Счётчик репостов
var lookClickCount = 1_099_998 //Счётчик просмотров
const val POSITIVE_INFINITY = 1.0/0.0 //Верхний предел счётчиков

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.textView)
        textView.movementMethod = LinkMovementMethod.getInstance()

        val heartButton =
            findViewById<ImageButton>(R.id.heartImageButton) //Тип параметра=кнопка с картинкой, id = heartImageButton
        val printLikes: TextView = findViewById(R.id.heartTextView) //Вывод результата в TextView
        heartButton.setOnClickListener {
            printLikes.text = heartIncrement()
        } //После нажатия вызывает функцию heartIncrement()

        val shareButton =
            findViewById<ImageButton>(R.id.shareImageButton) //Тип параметра=кнопка с картинкой, id = shareImageButton
        val printShares: TextView = findViewById(R.id.shareTextView) //Вывод результата в TextView
        shareButton.setOnClickListener {
            printShares.text = shareIncrement()
        } //После нажатия вызывает функцию shareIncrement()

        val lookButton =
            findViewById<ImageButton>(R.id.lookImageButton) //Тип параметра=кнопка с картинкой, id = shareImageButton
        val printLooks: TextView = findViewById(R.id.lookTextView) //Вывод результата в TextView
        lookButton.setOnClickListener {
            printLooks.text = lookIncrement()
        } //После нажатия вызывает функцию shareIncrement()
    }
}


fun heartIncrement(): String {
    heartClickCount++
    when (heartClickCount) {
        in 1000..1_099 -> {
            val text = (heartClickCount / 1000) //Выводит 1K лайков
            return text.toString() + "K"
        }
        in 1100..9_999 -> {
            var text = (heartClickCount.toDouble() / 1000) //Выводит 1.1K лайков
            text = (text * 10).roundToInt() / 10.0 // делить надо обязательно на тип Double
            return text.toString() + "K"
        }
        in 10_000..999_999 -> {
            val text = (heartClickCount / 1000) //Выводит 10K лайков
            return text.toString() + "K"
        }
        in 1_000_000..1_099_999 -> {
            val text = (heartClickCount / 1_000_000) //Выводит 1M лайков
            return text.toString() + "M"
        }
        in 1_100_000..POSITIVE_INFINITY.toInt() -> {
            var text =
                (heartClickCount.toDouble() / 1_000_000) //Выводит 1.1 лайков
            text = (text * 10).roundToInt() / 10.0 // делить надо обязательно на тип Double
            return text.toString() + "M"
        }
        else -> return heartClickCount.toString() //Преобразование Int в строку
    }
}


fun shareIncrement(): String {
    shareClickCount++
    when (shareClickCount) {
        in 1000..1_099 -> {
            val text = (heartClickCount / 1000)
            return text.toString() + "K"
        }
        in 1100..9_999 -> {
            var text = (heartClickCount.toDouble() / 1000)
            text = (text * 10).roundToInt() / 10.0
            return text.toString() + "K"
        }
        in 10_000..999_999 -> {
            val text = (heartClickCount / 1000)
            return text.toString() + "K"
        }
        in 1_000_000..1_099_999 -> {
            val text = (shareClickCount / 1_000_000) //Выводит количество миллионов репостов
            return text.toString() + "M"
        }
        in 1_100_000..POSITIVE_INFINITY.toInt() -> {
            var text =
                (heartClickCount.toDouble() / 1_000_000) //Выводит 1.1 репостов
            text = (text * 10).roundToInt() / 10.0 // делить надо обязательно на тип Double
            return text.toString() + "M"
        }
        else -> return shareClickCount.toString() //Преобразование Int в строку
    }
}


fun lookIncrement(): String { //Просмотры
    lookClickCount++
    when (lookClickCount) {
        in 1000..1_099 -> {
            val text = (heartClickCount / 1000)
            return text.toString() + "K"
        }
        in 1100..9_999 -> {
            var text = (heartClickCount.toDouble() / 1000)
            text = (text * 10).roundToInt() / 10.0
            return text.toString() + "K"
        }
        in 10_000..999_999 -> {
            val text = (heartClickCount / 1000)
            return text.toString() + "K"
        }
        in 1_000_000..1_099_999 -> {
            val text = (lookClickCount / 1_000_000)
            return text.toString() + "M"
        }
        in 1_100_000..POSITIVE_INFINITY.toInt() -> {
            var text = (lookClickCount.toDouble() / 1_000_000)
            text = (text * 10).roundToInt() / 10.0
            return text.toString() + "M"
        }
        else -> return lookClickCount.toString()
    }
}