package dev.lunarcoffee.blazelight.model.api.users

sealed class UserLoginResult

object UserLoginFailure : UserLoginResult()
class UserLoginSuccess(val id: Long) : UserLoginResult()
