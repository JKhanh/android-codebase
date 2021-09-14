package com.aibles.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aibles.common.utils.AppDispatchers
import com.aibles.common.utils.Resource
import com.aibles.home.domain.model.User
import com.aibles.home.domain.usecase.GetAllUserUseCase
import com.aibles.home.domain.usecase.QueryUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val queryUserUseCase: QueryUserUseCase,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val dispatchers: AppDispatchers
): ViewModel() {

    private var _userList: LiveData<Resource<List<User>>> = getAllUserUseCase().asLiveData()
    val userList: LiveData<Resource<List<User>>> get() = _userList

    fun getAllUser(query: String?){
        viewModelScope.launch(dispatchers.main) {
            _userList = if(!query.isNullOrBlank()) queryUserUseCase(query).asLiveData()
            else getAllUserUseCase().asLiveData()
        }
    }
}