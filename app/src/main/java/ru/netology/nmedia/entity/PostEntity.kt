package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    var likes: Long,
    var shares: Long,
    val looks: Long,
    val video: String
){
    fun toDto() = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likedByMe = likedByMe,
        likes = likes,
        shares = shares,
        looks = looks,
        video = video
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.author,
            dto.published,
            dto.content,
            dto.likedByMe,
            dto.likes,
            dto.shares,
            dto.looks,
            dto.video
        )
    }
}