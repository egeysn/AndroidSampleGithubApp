package com.egeysn.githubapp.presentation.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egeysn.githubapp.common.extension.addSimpleVerticalDecoration
import com.egeysn.githubapp.common.utils.UiText
import com.egeysn.githubapp.data.services.localStorage.LocalStorageService
import com.egeysn.githubapp.databinding.FragmentHomeBinding
import com.egeysn.githubapp.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment() :
    Fragment() {

    private lateinit var adapter: UserAdapter
    private val page = 1

    @Inject
    lateinit var localStorageService: LocalStorageService

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        listeners()
        setUpList()
        setupObservers()
        viewModel.getUsers(page)
        return binding.root
    }

    private fun listeners() =
        binding.cvSearch.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToSearchUserFragment()
            findNavController().navigate(directions)
        }

    private fun setupObservers() {
        viewModel.getViewState().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }.launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: HomeViewModel.HomeViewState) {
        when (state) {
            is HomeViewModel.HomeViewState.Init -> Unit
            is HomeViewModel.HomeViewState.Loading -> handleLoading(state.isLoading)
            is HomeViewModel.HomeViewState.Success -> handleSuccess(state.data)
            is HomeViewModel.HomeViewState.SuccessWithEmptyData -> Unit
            is HomeViewModel.HomeViewState.Error -> handleError(state.error)
        }
    }

    private fun handleSuccess(list: List<User>) = adapter.setItems(list)

    private fun handleLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun handleError(error: UiText) = Toast.makeText(requireContext(), error.asString(requireContext()), Toast.LENGTH_SHORT).show()

    private fun setUpList() {
        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvMovies.addSimpleVerticalDecoration(
            16, includeFirstItem = true, includeLastItem = true
        )
        adapter = UserAdapter(object : UserItemListener {
            override fun onUserClicked(username: String) {
                val directions = HomeFragmentDirections.actionHomeFragmentToUserDetailFragment().setUsername(username)
                findNavController().navigate(directions)
            }
        })
        binding.rvMovies.adapter = adapter
    }
}
