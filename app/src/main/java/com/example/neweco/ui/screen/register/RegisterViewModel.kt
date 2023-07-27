package com.example.neweco.ui.screen.register

import com.example.core.network.repository.AuthRepository
import com.example.core.viewmodels.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(repository: AuthRepository): AuthViewModel(repository) {
}