package com.egeysn.githubapp.presentation.user_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.egeysn.githubapp.common.extension.safeGet
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.data.services.localStorage.LocalStorageService
import com.egeysn.githubapp.databinding.FragmentUserDetailBinding
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.presentation.home.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserDetailFragment() :
    Fragment() {

    private lateinit var adapter: UserAdapter
    private val page = 1

    @Inject
    lateinit var localStorageService: LocalStorageService

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    private val args: UserDetailFragmentArgs by navArgs()

    companion object {
        private var MOVIE_ID: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        MOVIE_ID = args.username.safeGet()
        listeners()
        setupObservers()
        init()
        return binding.root
    }
    private fun setupObservers() {
        viewModel.getViewState().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }.launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: UserDetailViewModel.MovieDetailViewState) {
        when (state) {
            is UserDetailViewModel.MovieDetailViewState.Error -> handleError(state.error)
            UserDetailViewModel.MovieDetailViewState.Init -> UserDetailViewModel.MovieDetailViewState.Init
            is UserDetailViewModel.MovieDetailViewState.Loading -> handleLoading(state.isLoading)
            is UserDetailViewModel.MovieDetailViewState.Success -> handleSuccess(state.data)
        }
    }

    private fun handleSuccess(data: User) {
        binding.tvMovieTitle.text = data.name
        binding.tvDescription.text = data.name
        binding.tvGenre.text = data.name
        Glide.with(this).load(data.avatar).into(binding.ivMovie)
        /*     binding.tvDuration.text = data.runtime.runTimeToReadableDuration(this)
             binding.tvRating.text = getString(R.string.ratingWithParam, data.voteAverage)
             Glide.with(this).load(data.posterPath.toFullImageLink()).into(binding.ivMovie)*/
    }

    private fun handleLoading(loading: Boolean) {
        binding.progress.isVisible = loading
    }

    private fun handleError(error: UiText) = Toast.makeText(requireContext(), error.asString(requireContext()), Toast.LENGTH_SHORT).show()

    private fun init() = viewModel.getMovie(username = MOVIE_ID)

    private fun listeners() = binding.ivBack.setOnClickListener {
        // onBackPressedDispatcher.onBackPressed() }
    }
}
