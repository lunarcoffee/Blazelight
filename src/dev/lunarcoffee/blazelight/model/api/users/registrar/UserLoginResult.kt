package dev.lunarcoffee.blazelight.model.api.users.registrar

sealed class UserLoginResult

object UserLoginFailure : UserLoginResult()
class UserLoginSuccess(val id: Long) : UserLoginResult()
