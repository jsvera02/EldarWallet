package com.jsvera.eldarwallet.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.base.BaseDialog
import com.jsvera.eldarwallet.base.BaseFragment
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.data.adapter.CardAdapter
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.databinding.FragmentHomeBinding
import com.jsvera.eldarwallet.ui.addCard.AddCardActivity
import com.jsvera.eldarwallet.utils.isConnected
import com.jsvera.eldarwallet.viewModel.CardViewModel
import com.jsvera.eldarwallet.viewModel.UserViewModel

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val cardViewModel: CardViewModel by viewModels()

    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddCard.setOnClickListener {
            val i = Intent(requireContext(), AddCardActivity::class.java)
            startActivity(i)
        }
        val userId = AppPreferences.getUser()?.userId ?: 0
        cardViewModel.loadCards(userId)
        observeCards()
        observeUserData()
        loadUserData()

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
                    if (requireContext().isConnected()) {
                        showErrorDialog("Error", errorMessage)
                    } else {
                        val dialog = BaseDialog.newInstance(
                            getString(R.string.app_name),
                            getString(R.string.error_internet),
                            showBtnPositive = true,
                            textBtnPositive = getString(R.string.accept)
                        )
                        dialog.onClickAccept = {
                            dialog.dismiss()
                        }
                        dialog.show(childFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun observeUserData() {
        userViewModel.userLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvCards.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvCards.visibility = View.VISIBLE
                    val user = resource.data
                    user?.let {
                        binding.textViewBalance.text = String.format("Balance: %d", user.balance)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val errorMessage = resource.e ?: getString(R.string.unknown_error)
                    if (requireContext().isConnected()) {
                        showErrorDialog("Error", errorMessage.toString())
                    } else {
                        val dialog = BaseDialog.newInstance(
                            getString(R.string.app_name),
                            getString(R.string.error_internet),
                            showBtnPositive = true,
                            textBtnPositive = getString(R.string.accept)
                        )
                        dialog.onClickAccept = {
                            dialog.dismiss()
                        }
                        dialog.show(childFragmentManager, null)
                    }
                }
                }
            }
        }

    private fun loadUserData() {
        val userName = AppPreferences.getUser()?.userName
        userName?.let {
            userViewModel.loadUser(it)
        }
    }

    private fun setupRecyclerView(cards: List<CardEntity>) {
        val cardAdapter = CardAdapter(cards)
        binding.rvCards.apply {
            adapter = cardAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
