package com.aibles.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aibles.common.utils.AppDispatchers
import com.aibles.common.utils.Resource
import com.aibles.home.domain.model.User
import com.aibles.home.domain.usecase.GetAllUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllUserUseCase: GetAllUserUseCase,
    private val dispatchers: AppDispatchers
): ViewModel() {

    private var _userList: StateFlow<Resource<List<User>>> = MutableStateFlow(
        Resource.loading()
    )
    val userList: StateFlow<Resource<List<User>>> get() = _userList

    fun getAllUser(query: String){
        viewModelScope.launch(dispatchers.main) {
            _userList = getAllUserUseCase(query).stateIn(viewModelScope)
        }
    }
}