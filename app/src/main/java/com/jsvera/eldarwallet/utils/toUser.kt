package com.jsvera.eldarwallet.utils

import com.jsvera.eldarwallet.data.local.entities.UserEntity
import com.jsvera.eldarwallet.data.local.entities.User

fun UserEntity.toUser(): User {
    return User(
        userId = this.userId,
        userName = this.userName,
        name = this.name,
        lastName = this.lastName,
        password = this.password,
        balance = this.balance
    )
}