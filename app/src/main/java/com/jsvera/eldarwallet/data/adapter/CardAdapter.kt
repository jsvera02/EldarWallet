package com.jsvera.eldarwallet.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.databinding.ItemCardBinding
import com.jsvera.eldarwallet.utils.AppConstants.KEY_AMEX
import com.jsvera.eldarwallet.utils.AppConstants.KEY_MASTER
import com.jsvera.eldarwallet.utils.AppConstants.KEY_VISA
import com.jsvera.eldarwallet.utils.EncryptionUtil

class CardAdapter(
    private val cards: List<CardEntity>, val onClick: CardClickListener? = null,val context: Context
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: CardEntity) {
            val decryptedCardNumber = EncryptionUtil.decrypt(card.card_number)
            val decryptedCvv = EncryptionUtil.decrypt(card.cvv.toString())
            binding.root.setOnClickListener {
                onClick?.onCardClicked(card)
            }
            if (card.isSelected) {
                binding.cardContainer.strokeColor = ContextCompat.getColor(context, R.color.green_selected)
            } else {
                binding.cardContainer.strokeColor = ContextCompat.getColor(context, R.color.grey_disabled_light)
            }
            binding.tvCardNumber.text = formatCardNumber(decryptedCardNumber)
            binding.tvCardHolderName.text = card.card_holder_name
            binding.tvExpirationDate.text = card.expiration_date + "  "
            //  binding.tvCvv.text = decryptedCvv
            binding.ivCardLogo.setImageResource(getCardLogo(card.card_type))
        }

        private fun getCardLogo(cardType: String): Int {
            return when (cardType) {
                KEY_VISA -> R.drawable.ic_visa
                KEY_MASTER -> R.drawable.ic_master
                KEY_AMEX -> R.drawable.ic_amex
                else -> R.drawable.ic_tab_card_pay
            }
        }
    }

    fun formatCardNumber(cardNumber: String): String {
        val digitsOnly = cardNumber.replace("\\s".toRegex(), "")
        val maskedNumber = digitsOnly.takeLast(4).padStart(digitsOnly.length, '*')
        val formatted = StringBuilder()

        when {
            maskedNumber.startsWith("3") -> {
                for (i in maskedNumber.indices) {
                    if (i == 4 || i == 10) {
                        formatted.append(" ")
                    }
                    formatted.append(maskedNumber[i])
                }
            }

            maskedNumber.startsWith("4") || maskedNumber.startsWith("5") -> {
                for (i in maskedNumber.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ")
                    }
                    formatted.append(maskedNumber[i])
                }
            }

            else -> {
                return maskedNumber
            }
        }

        return formatted.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size

    interface CardClickListener {
        fun onCardClicked(card: CardEntity)
    }
}
