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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.egeysn.githubapp.common.extension.safeGet
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.data.services.localStorage.LocalStorageService
import com.egeysn.githubapp.databinding.FragmentUserDetailBinding
import com.egeysn.githubapp.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserDetailFragment() :
    Fragment() {

    @Inject
    lateinit var localStorageService: LocalStorageService

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    private val args: UserDetailFragmentArgs by navArgs()

    companion object {
        private var USER_ID: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        USER_ID = args.username.safeGet()
        listeners()
        setupObservers()
        init()
        return binding.root
    }
    private fun setupObservers() {
        viewModel.getViewState().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }.launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: UserDetailViewModel.UserDetail) {
        when (state) {
            is UserDetailViewModel.UserDetail.Error -> handleError(state.error)
            UserDetailViewModel.UserDetail.Init -> UserDetailViewModel.UserDetail.Init
            is UserDetailViewModel.UserDetail.Loading -> handleLoading(state.isLoading)
            is UserDetailViewModel.UserDetail.Success -> handleSuccess(state.data)
        }
    }

    private fun handleSuccess(data: User) {
        binding.tvUserName.text = data.name
        binding.tvFollowersCount.text = data.followers.safeGet().toString()
        binding.checkBoxFav.isChecked = viewModel.isFavUser(data.id)
        binding.tvLocation.text = data.location ?: "-"
        data.bio?.let {
            binding.tvDescription.text = data.name
        } ?: kotlin.run {
            binding.tvDescription.text = "Not found"
        }
        Glide.with(this).load(data.avatar)
            .into(binding.ivUser)
        binding.checkBoxFav.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.saveFavorite(data.id) else viewModel.deleteFavorite(data.id)
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.progress.isVisible = loading
    }

    private fun handleError(error: UiText) = Toast.makeText(requireContext(), error.asString(requireContext()), Toast.LENGTH_SHORT).show()

    private fun init() = viewModel.getUserDetail(username = USER_ID)

    private fun listeners() = binding.ivBack.setOnClickListener {
        findNavController().popBackStack()
    }
}
