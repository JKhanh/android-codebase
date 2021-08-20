package com.aibles.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aibles.common.ui.GithubDiscoverTheme
import com.aibles.home.domain.model.User
import com.aibles.home.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GithubDiscoverTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        ListUser()
                    }
                }
            }
        }
    }

    @Composable
    fun ListUser(){
        var query by remember { mutableStateOf("") }
        val userList by viewModel.userList.collectAsState()
        val localFocusManager = LocalFocusManager.current
        Column {
            OutlinedTextField(
                value = query,
                onValueChange = {query = it},
                label = { Text("Username")},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        localFocusManager.clearFocus()
                        viewModel.getAllUser(query)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Timber.d("ListUser: loading")
            if(userList.isSuccessful()) {
                Timber.d("ListUser: has data")
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = userList.data!!,
                        itemContent = {
                            UserItem(it)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun UserItem(user: User){
        Row {
            Text(user.login)
        }
    }
}