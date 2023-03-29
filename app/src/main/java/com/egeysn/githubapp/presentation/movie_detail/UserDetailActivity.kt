package com.egeysn.githubapp.presentation.movie_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.egeysn.githubapp.common.utils.Constants.Companion.ARG_ID
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.databinding.ActivityMovieDetailBinding
import com.egeysn.githubapp.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.getLong(ARG_ID)?.let { MOVIE_ID = it }
        listeners()
        setupObservers()
        init()
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
   /*     binding.tvDuration.text = data.runtime.runTimeToReadableDuration(this)
        binding.tvRating.text = getString(R.string.ratingWithParam, data.voteAverage)
        Glide.with(this).load(data.posterPath.toFullImageLink()).into(binding.ivMovie)*/
    }

    private fun handleLoading(loading: Boolean) {
        binding.progress.isVisible = loading
    }

    private fun handleError(error: UiText) = Toast.makeText(this, error.asString(this), Toast.LENGTH_SHORT).show()

    private fun init() = viewModel.getMovie(id = MOVIE_ID)

    private fun listeners() = binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

    companion object {
        private var MOVIE_ID: Long = 0
        fun createSimpleIntent(context: Context, movieId: Long): Intent =
            Intent(context, UserDetailActivity::class.java).putExtra(ARG_ID, movieId)
    }
}
