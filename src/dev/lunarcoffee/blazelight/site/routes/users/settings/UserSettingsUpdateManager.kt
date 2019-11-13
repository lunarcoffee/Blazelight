package dev.lunarcoffee.blazelight.site.routes.users.settings

import dev.lunarcoffee.blazelight.model.api.users.registrar.UserEditManager
import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.shared.language.Language
import java.time.ZoneId

object UserSettingsUpdateManager {
    suspend fun update(
        user: User,
        realName: String,
        description: String,
        timeZone: ZoneId,
        language: Language,
        themeName: String
    ): UserSettingsUpdateResult {

        if (realName.length > 100)
            return UserSettingsUpdateResult.INVALID_REAL_NAME
        if (description.length > 500)
            return UserSettingsUpdateResult.INVALID_DESCRIPTION

        val newUserSettings = user.settings.copy(
            zoneId = timeZone,
            language = language,
            theme = themeName
        )

        val newUser = user.apply {
            this@apply.realName = realName.ifEmpty { null }
            this@apply.description = description.ifEmpty { null }
            settings = newUserSettings
        }

        // Edit the settings, and send them back to their user page.
        UserEditManager.edit(newUser)
        return UserSettingsUpdateResult.SUCCESS
    }
}
