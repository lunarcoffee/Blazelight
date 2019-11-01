package dev.lunarcoffee.blazelight.shared.language

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.pipeline.PipelineContext

private val PipelineContext<*, ApplicationCall>.language
    get() = call.sessions.get<UserSession>()?.getUser()?.settings?.language
        ?: LanguageManager.toLanguage(BL_CONFIG.defaultLangCode)

val PipelineContext<*, ApplicationCall>.s get() = language.strings

// Replaces percent template codes in language files with [args].
fun String.prep(vararg args: String): String {
    var res = this
    for ((index, arg) in args.withIndex())
        res = res.replace("%$index", arg)
    return res
}
