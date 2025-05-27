package com.project.tuntun.data.manager.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.project.tuntun.utils.extensions.datastore
import com.project.tuntun.data.utils.PreferenceDatastoreKeys
import com.project.tuntun.domain.manager.datastore.LocalUserConfigManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserConfigManagerImpl @Inject constructor(
    private val context: Context
): LocalUserConfigManager {

    override suspend fun writeUserConfig() {
        context.datastore.edit { userConfigDatastore ->
            userConfigDatastore[PreferenceDatastoreKeys.USER_CONFIG] = true
        }
    }

    override fun readUserConfig(): Flow<Boolean> {
        return context.datastore.data
            .map { preferences ->
                preferences[PreferenceDatastoreKeys.USER_CONFIG] ?: false
            }
    }
}