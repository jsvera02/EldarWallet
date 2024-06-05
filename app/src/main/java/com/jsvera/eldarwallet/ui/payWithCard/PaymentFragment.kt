package com.jsvera.eldarwallet.ui.payWithCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.base.BaseDialog
import com.jsvera.eldarwallet.base.BaseFragment
import com.jsvera.eldarwallet.data.adapter.CardAdapter
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.databinding.FragmentPaymentBinding
import com.jsvera.eldarwallet.utils.gone
import com.jsvera.eldarwallet.utils.invisible
import com.jsvera.eldarwallet.utils.visible
import com.jsvera.eldarwallet.viewModel.CardViewModel
import com.jsvera.eldarwallet.viewModel.PaymentViewModel

class PaymentFragment : BaseFragment(), CardAdapter.CardClickListener {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardAdapter: CardAdapter
    private var hasDialogBeenShown = false

    private val cardViewModel: CardViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCards()
    }

    private fun loadCards() {
        cardViewModel.loadCards(AppPreferences.getUser()?.userId ?: 0)
        observeCards()
    }

    private fun observeCards() {
        cardViewModel.cardsLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                }

                is Resource.Success -> {
                    if (resource.data?.size == 0)
                        showDialog("Eldar Wallet", "Ingresa una tarjeta primero")
                    else {
                        binding.progressBar.gone()
                        binding.containerCard.visible()
                        val cards = resource.data ?: emptyList()
                        setupRecyclerView(cards)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.containerCard.gone()
                    val errorMessage = resource.e?.message ?: getString(R.string.unknown_error)
                    showErrorDialog("Error", errorMessage)
                }
            }

            paymentViewModel.paymentCompleted.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    true -> {
                        showDialog("Eldar Wallet", "Pago realizado con éxito")
                        binding.btnPay.isEnabled = true
                        binding.btnPay.visible()
                    }

                    false -> {
                        binding.progressBarPay.visible()
                    }
                }
            }

            paymentViewModel.selectedCard.observe(viewLifecycleOwner) { selectedCard ->
                binding.containerAmount.visible()

                binding.btnPay.setOnClickListener {
                    if (binding.etAmount.text.toString().toInt()>0) {
                        binding.progressBarPay.visible()
                        binding.btnPay.invisible()
                        paymentViewModel.simulatePayment()
                        binding.btnPay.isEnabled = false
                    }else showErrorDialog("Eldar Wallet", "Ingrese un monto mayor a 0")
                }
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        if (!hasDialogBeenShown) {
            hasDialogBeenShown = true
            val dialogCard = BaseDialog.newInstance(
                title,
                message,
                showBtnPositive = true,
                textBtnPositive = getString(R.string.accept)
            )
            dialogCard.onClickAccept = {
                goToHome()
                dialogCard.dismiss()
            }
            dialogCard.show(childFragmentManager, null)
        }
    }

    private fun setupRecyclerView(cards: List<CardEntity>) {
        cardAdapter = CardAdapter(cards, object : CardAdapter.CardClickListener {
            override fun onCardClicked(card: CardEntity) {
                paymentViewModel.selectCard(card.toCard())
                card.isSelected = true
                cardAdapter.notifyDataSetChanged()
            }
        }, requireContext())
        binding.rvCards.apply {
            adapter = cardAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCardClicked(card: CardEntity) {
        paymentViewModel.selectCard(card.toCard())
    }
}