package com.jsvera.eldarwallet.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.jsvera.eldarwallet.utils.EncryptionUtil


@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    val cardId: Long = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Long,

    @ColumnInfo(name = "card_number")
    val card_number: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "card_holder_name")
    val card_holder_name: String,

    @ColumnInfo(name = "card_type")
    val card_type: String,

    @ColumnInfo(name = "expiration_date")
    val expiration_date: String,

    @ColumnInfo(name = "cvv")
    val cvv: String,

    @ColumnInfo(name = "dni")
    val dni: String
) {
    fun toCard(): Card {
        return Card(
            cardId = this.cardId,
            userId = this.user_id,
            cardNumber = EncryptionUtil.decrypt(this.card_number),
            name = this.name,
            cardHolderName = this.card_holder_name,
            cardType = this.card_type,
            cvv =EncryptionUtil.decrypt(this.cvv),
            expirationDate = this.expiration_date,
            dni = this.dni
        )
    }
}
