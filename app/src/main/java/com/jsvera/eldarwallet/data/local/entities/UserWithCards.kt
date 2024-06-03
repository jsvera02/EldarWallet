package com.jsvera.eldarwallet.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.jsvera.eldarwallet.data.local.database.CardEntity
import com.jsvera.eldarwallet.data.local.database.UserEntity

data class UserWithCards(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val cards: List<CardEntity>
) {
    fun toUser(): User {
        val userToReturn = User(user.name, user.lastName, user.userName, user.password)
        userToReturn.balance = user.balance
        userToReturn.id = user.userId
        val cardList = mutableListOf<Card>()
        cards.forEach { cardList.add(it.toCard()) }
        userToReturn.cards = cardList
        return userToReturn
    }
}