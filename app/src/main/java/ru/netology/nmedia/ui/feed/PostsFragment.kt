package ru.netology.nmedia.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.FeedAdapter
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.databinding.FragmentPostsBinding
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.ui.MediaLifecycleObserver
import ru.netology.nmedia.ui.attachment.ImageFragment
import ru.netology.nmedia.ui.attachment.VideoFragment
import ru.netology.nmedia.ui.profile.UserFragment
import ru.netology.nmedia.viewmodel.PostViewModel

class PostsFragment : Fragment() {

    private val postViewModel by activityViewModels<PostViewModel>()
    private val mediaObserver = MediaLifecycleObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = FeedAdapter(object : OnInteractionListener {
            override fun onLike(feedItem: FeedItem) {
                postViewModel.likeById(feedItem as Post)
            }

            override fun onRemove(id: FeedItem) {
                TODO("Not yet implemented")
            }

            override fun onEdit(feedItem: FeedItem) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
                postViewModel.edit(feedItem as Post)
            }

            override fun onUser(userId: Int) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_userFragment,
                    bundleOf(
                        UserFragment.USER_ID to userId
                    )
                )
            }

            override fun onPlayPause(feedItem: FeedItem) {
                if (feedItem.attachment?.type == AttachmentType.AUDIO) {
                    feedItem.attachment?.url?.let { mediaObserver.playPause(it) }
                }
            }

            override fun onCoordinates(lat: Double, long: Double) {
                TODO("Not yet implemented")
            }

            override fun onVideo(url: String) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_videoFragment,
                    bundleOf(
                        VideoFragment.URL to url
                    )
                )
            }

            override fun onImage(url: String) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_imageFragment,
                    bundleOf(
                        ImageFragment.URL to url
                    )
                )
            }
        })

        binding.listPosts.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postViewModel.data.collectLatest { data ->
                    adapter.submitData(data)
                }
            }
        }

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swiperefresh.isRefreshing =
                        state.refresh is LoadState.Loading
                }
            }
        }

        mediaObserver.player?.setOnCompletionListener {
            mediaObserver.player?.stop()
        }

        binding.swiperefresh.setOnRefreshListener(adapter::refresh)


        return binding.root
    }
}