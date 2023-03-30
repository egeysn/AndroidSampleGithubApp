package com.egeysn.githubapp.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import com.egeysn.githubapp.databinding.FragmentSearchBinding
import com.egeysn.githubapp.domain.models.User
import com.egeysn.githubapp.presentation.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchUserFragment() :
    Fragment() {

    private lateinit var adapter: SearchUserAdapter

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.etSearch.requestFocus()
        setUpList()
        listeners()
        setupObservers()
        return binding.root
    }
    private fun setupObservers() {
        viewModel.getViewState().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }.launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: SearchViewModel.SearchViewState) {
        when (state) {
            is SearchViewModel.SearchViewState.Error -> handleError(state.error)
            SearchViewModel.SearchViewState.Init -> Unit
            is SearchViewModel.SearchViewState.Loading -> handleLoading(state.isLoading)
            is SearchViewModel.SearchViewState.Success -> handleSuccess(state.data)
        }
    }

    private fun handleSuccess(data: List<User>) {
        binding.viewError.tvError.visibility = View.GONE
        adapter.setItems(data)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun handleError(error: UiText) {
        binding.viewError.tvError.visibility = View.VISIBLE
        adapter.setItems(arrayListOf())
        binding.viewError.tvError.text = error.asString(requireContext())
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard()
            }
            true
        }
        binding.etSearch.addTextChangedListener { text ->
            if (text != null && text.length > 1) {
                viewModel.searchMovie(text.toString())
            }
        }
    }

    private fun setUpList() {
        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvMovies.addSimpleVerticalDecoration(
            16, includeFirstItem = true, includeLastItem = true
        )
        adapter = SearchUserAdapter(object : UserItemListener {
            override fun onUserClicked(userName: String) {
                val directions = HomeFragmentDirections.actionHomeFragmentToUserDetailFragment().setUsername(userName)
                findNavController().navigate(directions)
            }
        })
        binding.rvMovies.adapter = adapter
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
            AppCompatActivity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        }
    }
}
