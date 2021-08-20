package com.aibles.home.data.repository

import com.aibles.common.utils.AppDispatchers
import com.aibles.common.utils.Resource
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
    override suspend fun getAllUser(query: String): Flow<Resource<List<User>>> {
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
//    override suspend fun getAllUser(query: String): Flow<Resource<List<User>>> =
//        performGetOperation(
//            databaseQuery = {
//                dao.findUserWithName(query).map {
//                    it.map {
//                        it.mapToDomain()
//                    }
//                }
//            },
//            networkCall = {
//                dataSource.getAllUser(query).map {
//                    it?.items
//                }
//            },
//            saveCallResult = { list ->
//                list?.let {
//                    dao.saveUser(list.map {
//                        it.mapToLocal()
//                    })
//                }
//            }
//        )
}