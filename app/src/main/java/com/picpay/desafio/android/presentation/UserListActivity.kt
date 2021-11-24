package com.picpay.desafio.android.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.ActivityUserListBinding
import com.picpay.desafio.android.presentation.adapter.UserListAdapter
import com.picpay.desafio.android.presentation.state.ViewEvent
import com.picpay.desafio.android.presentation.state.ViewState
import com.picpay.desafio.android.util.extensions.showToast
import com.picpay.desafio.android.util.extensions.visible
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUserListBinding.inflate(layoutInflater) }
    private val userListAdapter = UserListAdapter()
    private val viewModel: UserListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.run {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(this@UserListActivity)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    launch { uiState.collect { state -> handleStateChange(state) } }
                    launch { events.collect { event -> handleEvent(event) } }
                }
            }
        }
    }

    private fun handleStateChange(state: ViewState) {
        binding.userListProgressBar.visible(state.isLoading)
        userListAdapter.submitList(state.users)
    }

    private fun handleEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.ShowFetchErrorToast -> showToast()
        }
    }
}
