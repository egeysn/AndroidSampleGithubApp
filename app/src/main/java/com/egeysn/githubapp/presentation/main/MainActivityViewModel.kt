package com.egeysn.githubapp.presentation.main

import androidx.lifecycle.ViewModel
import com.egeysn.githubapp.domain.use_cases.home.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getMoviesUseCase: GetUsersUseCase
) : ViewModel()
