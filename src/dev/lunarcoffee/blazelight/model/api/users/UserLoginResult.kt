package dev.lunarcoffee.blazelight.model.api.users

sealed class UserLoginResult

object UserLoginFailure : UserLoginResult()
object UserLoginToContinue : UserLoginResult()
class UserLoginSuccess(val id: Long) : UserLoginResult()
