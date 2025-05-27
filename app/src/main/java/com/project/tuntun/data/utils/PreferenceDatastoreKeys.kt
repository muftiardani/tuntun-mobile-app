package com.project.tuntun.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.project.tuntun.utils.Constants

object PreferenceDatastoreKeys {
    val USER_CONFIG = booleanPreferencesKey(name = Constants.USER_CONFIG)
}