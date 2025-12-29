package com.defacto.movieapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.defacto.movieapp.data.local.datastore.UserPreferencesKeys
import com.defacto.movieapp.data.local.datastore.userDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val userIdFlow: Flow<Long?> =
        context.userDataStore.data.map { prefs ->
            prefs[UserPreferencesKeys.LOGGED_USER_ID]
        }

    suspend fun saveUserId(userId: Long) {
        context.userDataStore.edit { prefs ->
            prefs[UserPreferencesKeys.LOGGED_USER_ID] = userId
        }
    }

    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
}
