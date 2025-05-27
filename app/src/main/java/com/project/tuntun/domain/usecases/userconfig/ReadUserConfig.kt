package com.project.tuntun.domain.usecases.userconfig

import com.project.tuntun.domain.manager.datastore.LocalUserConfigManager
import kotlinx.coroutines.flow.Flow

class ReadUserConfig(
    private val userConfigManager: LocalUserConfigManager
) {

    operator fun invoke(): Flow<Boolean> {
        return userConfigManager.readUserConfig()
    }
}