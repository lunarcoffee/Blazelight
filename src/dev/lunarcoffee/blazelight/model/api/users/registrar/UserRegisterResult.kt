package dev.lunarcoffee.blazelight.model.api.users.registrar

enum class UserRegisterResult {
    SUCCESS,
    INVALID_EMAIL,
    INVALID_NAME,
    INVALID_PASSWORD,
    DUPLICATE_EMAIL,
    DUPLICATE_USERNAME
}
