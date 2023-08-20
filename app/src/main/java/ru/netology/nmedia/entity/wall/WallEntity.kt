package ru.netology.nmedia.entity.wall

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class WallEntity(
    @PrimaryKey
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String? = null,
    val content: String,
    val published: String,
    val link: String? = null,
    val likeOwnerIds: List<Int> = emptyList(),
    val mentionIds: List<Int> = emptyList(),
    val likedByMe: Boolean,
    @Embedded
    val attachment: Attachment? = null,
    val ownedByMe: Boolean,
) {
    fun toDto() = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        likedByMe = likedByMe,
        attachment = attachment,
        ownedByMe = ownedByMe,
    )

    companion object {
        fun fromDto(post: Post) = WallEntity(
            id = post.id,
            authorId = post.authorId,
            author = post.author,
            authorAvatar = post.authorAvatar,
            content = post.content,
            published = post.published,
            likedByMe = post.likedByMe,
            attachment = post.attachment,
            ownedByMe = post.ownedByMe,
        )
    }
}