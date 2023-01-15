package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false  //Лайк включен или отключен
)
//compileSdk 32
//dependencies {
//
//    implementation 'androidx.core:core-ktx:1.9.0'