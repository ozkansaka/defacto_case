package com.defacto.movieapp.data.repository

import com.defacto.movieapp.data.local.dao.SearchHistoryDao
import com.defacto.movieapp.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {

    suspend fun addSearchHistory(query: String) {
        searchHistoryDao.insert(SearchHistoryEntity(query = query))
    }

    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches()
    }
}
