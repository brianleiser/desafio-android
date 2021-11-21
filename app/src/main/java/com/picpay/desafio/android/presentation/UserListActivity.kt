package com.picpay.desafio.android.presentation

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class UserListActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter

    private val getUsers: GetUsers by inject()

    override fun onResume() {
        super.onResume()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            runCatching {
                val result = withContext(Dispatchers.IO) { getUsers() }
                progressBar.visibility = View.GONE
                return@runCatching result
            }.onSuccess { users: Set<User> ->
                adapter.users = users.toList()
            }.onFailure { err: Throwable ->
                val message = getString(R.string.error)
                Toast
                    .makeText(this@UserListActivity, message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
