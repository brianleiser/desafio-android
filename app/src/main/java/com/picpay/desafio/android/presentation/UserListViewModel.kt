package com.picpay.desafio.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.usecase.GetUsers
import com.picpay.desafio.android.domain.Result
import com.picpay.desafio.android.presentation.state.ViewEvent
import com.picpay.desafio.android.presentation.state.ViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserListViewModel(private val getUsers: GetUsers) : ViewModel() {

    private val mutableUiState = MutableStateFlow(ViewState())
    val uiState: StateFlow<ViewState> = mutableUiState

    private val eventsChannel = Channel<ViewEvent>(Channel.BUFFERED)
    val events: Flow<ViewEvent> = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            reduceUsersList()
        }
    }

    suspend fun reduceUsersList() {
        getUsers().collect { result ->
            when (result) {
                is Result.Success -> {
                    mutableUiState.value = uiState.value.copy(isLoading = false, users = result.data)
                }
                is Result.Error -> {
                    mutableUiState.value = uiState.value.copy(isLoading = false)
                    eventsChannel.send(ViewEvent.ShowFetchErrorToast)
                }
                is Result.Loading -> mutableUiState.value = uiState.value.copy(isLoading = true)
                is Result.Idle -> mutableUiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }
}