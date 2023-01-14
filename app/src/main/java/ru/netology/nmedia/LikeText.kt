package ru.netology.nmedia

import kotlin.math.roundToInt

fun likeText(elementCount: Int): String {
    return when (elementCount) {
        in 0..999 -> elementCount.toString()
        in 1000..1_099 -> {(elementCount / 1000).toString() + "K" //Outputs 1K likes
        }
        in 1100..9_999 -> {
            var text = (elementCount.toDouble() / 1000) //Выводит 1.1K лайков
            text = (text * 10).roundToInt() / 10.0 // делить надо обязательно на тип Double
            text.toString() + "K"
        }
        in 10_000..999_999 -> {val text = (elementCount / 1000) //Выводит 10K лайков
            text.toString() + "K"
        }
        in 1_000_000..1_099_999 -> {val text = (elementCount / 1_000_000) //Выводит 1M лайков
            text.toString() + "M"
        }
        else -> {var text = (elementCount.toDouble() / 1_000_000) //Выводит 1.1M лайков
            text = (text * 10).roundToInt() / 10.0 // делить надо обязательно на тип Double
            text.toString() + "M"
        }
    }
}