package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.MediaModel
import ru.netology.nmedia.repository.post.PostRepository
import ru.netology.nmedia.utils.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Post(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = null,
    authorJob = null,
    link = null,
    content = "",
    published = "",
    mentionedMe = false,
    users = emptyMap()
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {
    private val cached = repository
        .data
        .cachedIn(viewModelScope)

    val data: Flow<PagingData<FeedItem>> = cached


    fun wallData(userId: Int): Flow<PagingData<FeedItem>> = repository.userWall(userId)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post>
        get() = _edited

    private val _media = MutableLiveData<MediaModel?>(null)
    val media: LiveData<MediaModel?>
        get() = _media

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun addMedia(uri: Uri, file: File, type: AttachmentType) {
        _media.value = MediaModel(uri, file, type)
    }

    fun clearMedia() {
        _media.value = null
        _edited.value = _edited.value?.copy(attachment = null)
    }

    fun save() {
        edited.value?.let {
            if (it !== empty) {
                viewModelScope.launch {
                    try {
                        when (val media = media.value) {
                            null -> repository.save(it)
                            else -> {
                                repository.saveWithAttachment(it, media)
                            }
                        }
                        _postCreated.value = Unit
                        clearEdited()
                        clearMedia()
                        _dataState.value = FeedModelState()
                    } catch (e: Exception) {
                        _dataState.value = FeedModelState(error = true)
                    }
                }
            }
        }
    }

    fun edit(post: Post) {
        _edited.value = post
    }

    fun clearEdited() {
        _edited.value = empty
    }

    fun getEditPost(): Post? {
        return if (edited.value == null || edited.value == empty) null else edited.value
    }

    fun changeContent(content: String, link: String?) {
        val text = content.trim()
        _edited.value = edited.value?.copy(content = text, link = link)
    }

    fun likeById(post: Post) {
        viewModelScope.launch {
            try {
                repository.likeById(post)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }

        }
    }

    fun removeById(id: Int) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun wallRemoveById(id: Int) {
        removeById(id)
        viewModelScope.launch {
            repository.wallRemoveById(id)
        }
    }
}