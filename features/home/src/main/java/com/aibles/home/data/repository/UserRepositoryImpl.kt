package com.aibles.home.data.repository

import com.aibles.common.utils.Resource
import com.aibles.common.utils.map
import com.aibles.common.utils.performGetOperation
import com.aibles.home.data.local.UserDao
import com.aibles.home.data.remote.GithubDataSource
import com.aibles.home.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val dataSource: GithubDataSource
): UserRepository {
    override suspend fun getAllUser(query: String): Flow<Resource<List<User>>> =
        performGetOperation(
            databaseQuery = {
                dao.getAllUser().map {
                    it.map {
                        it.mapToDomain()
                    }
                }
            },
            networkCall = {
                dataSource.getAllUser(query).map {
                    it?.items
                }
            },
            saveCallResult = { list ->
                list?.let {
                    dao.saveUser(list.map {
                        it.mapToLocal()
                    })
                }
            }
        )
}