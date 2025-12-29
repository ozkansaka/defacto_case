package com.defacto.movieapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore(
    name = "user_session"
)

object UserPreferencesKeys {
    val LOGGED_USER_ID = longPreferencesKey("logged_user_id")
}
