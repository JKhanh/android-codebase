package com.aibles.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.aibles.common.ui.GithubDiscoverTheme
import com.aibles.common.utils.Resource
import com.aibles.home.domain.model.User
import com.aibles.home.presentation.viewmodel.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private val viewModel by viewModels<HomeViewModel>()

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GithubDiscoverTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        HomeScreen(viewModel)
                    }
                }
            }
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun HomeScreen(viewModel: HomeViewModel){
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        var query by remember { mutableStateOf("") }
        val userList by viewModel.userList.observeAsState(Resource.loading())
        val keyboardController = LocalSoftwareKeyboardController.current
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
        Column {
            OutlinedTextField(
                value = query,
                onValueChange = {query = it},
                label = { Text("Username")},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.getAllUser(query)
                        keyboardController?.hide()
                    }
                ),
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp, top = 8.dp)
            )
            if(userList.isSuccessful()) {
                ListUser(userList = userList.data!!)
            }
        }
    }

    @Composable
    fun ListUser(userList: List<User>){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = userList,
                itemContent = {
                    UserItem(it)
                }
            )
        }
    }

    @Composable
    fun UserItem(user: User){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(10.dp),
            contentColor = Color.LightGray,
            elevation = 12.dp
        ) {
            Row {
                Avatar(user.avatarUrl)
                Spacer(modifier = Modifier.size(16.dp))
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(user.login, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = user.htmlUrl)
                    Text(text = user.score.toString())
                }
            }
        }
    }

    @Composable
    fun Avatar(url: String){
        Image(
            painter = rememberImagePainter(
                data = url,
                builder = {
                    transformations(CircleCropTransformation())
                    crossfade(true)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
        )
    }
}