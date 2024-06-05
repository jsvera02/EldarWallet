package com.jsvera.eldarwallet.ui.payWithCard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.base.BaseFragment
import com.jsvera.eldarwallet.data.adapter.CardAdapter
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.databinding.FragmentHomeBinding
import com.jsvera.eldarwallet.databinding.FragmentPaymentBinding
import com.jsvera.eldarwallet.viewModel.CardViewModel
import com.jsvera.eldarwallet.viewModel.PaymentViewModel

class PaymentFragment : BaseFragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val cardViewModel: CardViewModel by viewModels()
    private val payMentviewModel: PaymentViewModel by viewModels()

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
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvCards.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvCards.visibility = View.VISIBLE
                    val cards = resource.data ?: emptyList()
                    setupRecyclerView(cards)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = resource.e?.message ?: getString(R.string.unknown_error)
                    showErrorDialog("Error", errorMessage)
                }
            }
            payMentviewModel.selectedCard.observe(viewLifecycleOwner) { selectedCard ->

            }
        }
    }

    private fun setupRecyclerView(cards: List<CardEntity>) {
        val cardAdapter = CardAdapter(cards)
        binding.rvCards.apply {
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}