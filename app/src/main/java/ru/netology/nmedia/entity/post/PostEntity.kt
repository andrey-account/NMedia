package ru.netology.nmedia.entity.post

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.*

@Entity
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String? = null,
    val authorJob: String?,
    val content: String,
    val published: String,
    val link: String? = null,
    val likeOwnerIds: List<Int> = emptyList(),
    val mentionIds: List<Int> = emptyList(),
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: Attachment? = null,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview> = emptyMap(),
) {
    fun toDto() = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        authorJob = authorJob,
        content = content,
        published = published,
        link = link,
        likeOwnerIds = likeOwnerIds,
        mentionIds = mentionIds,
        mentionedMe = mentionedMe,
        likedByMe = likedByMe,
        attachment = attachment,
        ownedByMe = ownedByMe,
        users = users,
    )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            id = post.id,
            authorId = post.authorId,
            author = post.author,
            authorAvatar = post.authorAvatar,
            authorJob = post.authorJob,
            content = post.content,
            published = post.published,
            link = post.link,
            likeOwnerIds = post.likeOwnerIds,
            mentionIds = post.mentionIds,
            mentionedMe = post.mentionedMe,
            likedByMe = post.likedByMe,
            attachment = post.attachment,
            ownedByMe = post.ownedByMe,
            users = post.users,
        )
    }
}

fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)