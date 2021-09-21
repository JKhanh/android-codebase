package com.aibles.home.data.repository

import com.aibles.common.utils.AppDispatchers
import com.aibles.common.utils.Resource
import com.aibles.common.utils.map
import com.aibles.common.utils.networkBoundResource
import com.aibles.home.data.local.UserDao
import com.aibles.home.data.remote.GithubDataSource
import com.aibles.home.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val dataSource: GithubDataSource,
    private val dispatchers: AppDispatchers
): UserRepository {
    override suspend fun queryUser(query: String): Flow<Resource<List<User>>> {
        return networkBoundResource(
            fetchFromLocal = {
                 dao.findUserWithName("%$query%").map {
                     it.map {
                         it.mapToDomain()
                     }
                 }
            },
            fetchFromRemote = { dataSource.getAllUser(query) },
            saveRemoteData = { dao.saveUser(it.items.map {
                it.mapToLocal()
            }) },
            onFetchFailed = {
                Timber.e("getAllUser error: $it")
            }
        ).flowOn(dispatchers.io)
    }

    override fun getAllUser(): Flow<Resource<List<User>>> =
        dao.getAllUser().map {
            it?.let {
                Resource.success(it).map {
                    it!!.map {
                        it.mapToDomain()
                    }
                }
            }
        }
}