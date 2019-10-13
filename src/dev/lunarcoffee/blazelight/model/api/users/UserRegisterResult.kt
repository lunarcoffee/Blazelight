package dev.lunarcoffee.blazelight.model.api.users

sealed class UserRegisterResult

object UserRegisterSuccess : UserRegisterResult()
object UserRegisterInvalidEmail : UserRegisterResult()
object UserRegisterInvalidName : UserRegisterResult()
object UserRegisterInvalidPassword : UserRegisterResult()
object UserRegisterDuplicateEmail : UserRegisterResult()
object UserRegisterDuplicateName : UserRegisterResult()
