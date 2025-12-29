package com.defacto.movieapp.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defacto.movieapp.data.repository.AuthRepository
import com.defacto.movieapp.data.remote.util.Result
import com.defacto.movieapp.data.remote.util.UiEvent
import com.defacto.movieapp.data.remote.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<Unit>())
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()


    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            when (val result = authRepository.register(email, password)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvents.emit(UiEvent.Action())
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvents.emit(UiEvent.ShowDialog(result.code, result.message))
                }
            }
        }
    }
}
