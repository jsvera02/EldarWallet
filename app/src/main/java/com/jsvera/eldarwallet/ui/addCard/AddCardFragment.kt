package com.jsvera.eldarwallet.ui.addCard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.databinding.FragmentAddCardBinding

class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveCard.setOnClickListener {
            saveCard()
        }
    }

    private fun saveCard() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}