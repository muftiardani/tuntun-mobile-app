package com.project.tuntun.domain.usecases.userconfig

import com.project.tuntun.domain.manager.datastore.LocalUserConfigManager

class WriteUserConfig(
    private val userConfigManager: LocalUserConfigManager
) {

    suspend operator fun invoke() {
        userConfigManager.writeUserConfig()
    }
}