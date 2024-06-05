package com.jsvera.eldarwallet.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.local.entities.Card
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.databinding.ItemCardBinding
import com.jsvera.eldarwallet.utils.AppConstants.KEY_AMEX
import com.jsvera.eldarwallet.utils.AppConstants.KEY_MASTER
import com.jsvera.eldarwallet.utils.AppConstants.KEY_VISA
import com.jsvera.eldarwallet.utils.EncryptionUtil

class CardAdapter(private val cards: List<CardEntity>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: CardEntity) {
            val decryptedCardNumber = EncryptionUtil.decrypt(card.card_number)
            val decryptedCvv = EncryptionUtil.decrypt(card.cvv.toString())

            binding.tvCardNumber.text = decryptedCardNumber
            binding.tvCardHolderName.text = card.card_holder_name
            binding.tvExpirationDate.text = card.expiration_date
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size
}
