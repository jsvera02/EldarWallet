package com.jsvera.eldarwallet.data.base

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.databinding.BaseDialogBinding
import com.jsvera.eldarwallet.utils.JellyInterpolator
import com.jsvera.eldarwallet.utils.gone
import com.jsvera.eldarwallet.utils.maskEmail

class BaseDialog : DialogFragment() {
    private var _binding: BaseDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var onClickAccept: (() -> Unit?)
    lateinit var onClickCancel: (() -> Unit?)

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_SHOW_BTN_POSITIVE = "show_btn_positive"
        private const val ARG_TEXT_BTN_POSITIVE = "text_btn_positive"
        private const val ARG_SHOW_BTN_NEGATIVE = "show_btn_negative"
        private const val ARG_TEXT_BTN_NEGATIVE = "text_btn_negative"
        private const val ARG_SHOW_DRAWABLE = "show_drawable"
        private const val ARG_DRAWABLE = "drawable"
        private const val ARG_CANCELABLE = "is_cancelable"


        fun newInstance(
            title: String?,
            description: String?,
            showBtnPositive: Boolean = true,
            textBtnPositive: String? = null,
            showBtnNegative: Boolean = false,
            textBtnNegative: String? = null,
            showDrawable: Boolean? = false,
            drawable: Int? = null,
            cancelable: Boolean?=null
        ): BaseDialog {
            val fragment = BaseDialog()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putBoolean(ARG_SHOW_BTN_POSITIVE, showBtnPositive)
            args.putString(ARG_TEXT_BTN_POSITIVE, textBtnPositive)
            args.putBoolean(ARG_SHOW_BTN_NEGATIVE, showBtnNegative)
            args.putString(ARG_TEXT_BTN_NEGATIVE, textBtnNegative)
            args.putBoolean(ARG_SHOW_DRAWABLE, showDrawable ?: false)
            if (drawable != null) {
                args.putInt(ARG_DRAWABLE, drawable)
            }
            args.putBoolean(ARG_CANCELABLE, cancelable ?: false)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BaseDialogBinding.inflate(inflater, container, false)
        animateEntrance(binding.mContainer)
        val args = requireArguments()
        val title = args.getString(ARG_TITLE)
        val description = args.getString(ARG_DESCRIPTION)
        val showBtnPositive = args.getBoolean(ARG_SHOW_BTN_POSITIVE)
        val textBtnPositive = args.getString(ARG_TEXT_BTN_POSITIVE)
        val showBtnNegative = args.getBoolean(ARG_SHOW_BTN_NEGATIVE)
        val textBtnNegative = args.getString(ARG_TEXT_BTN_NEGATIVE)
        val showDrawable = args.getBoolean(ARG_SHOW_DRAWABLE)
        val drawable = args.getInt(ARG_DRAWABLE)
        val cancelable = args.getBoolean(ARG_CANCELABLE)

        configureDialog(
            title, description,showBtnPositive, textBtnPositive,
            showBtnNegative, textBtnNegative, showDrawable, drawable,cancelable
        )

        if (!showDrawable) {
            binding.ivIconDialog.isGone = true
        } else {
            binding.ivIconDialog.isGone = false
            binding.ivIconDialog.setImageResource(drawable)
        }

        return binding.root
    }

    private fun configureDialog(
        title: String?,
        description: String?,
        showBtnPositive: Boolean,
        textBtnPositive: String?,
        showBtnNegative: Boolean,
        textBtnNegative: String?,
        showDrawable: Boolean?,
        drawable: Int,
        cancelable:Boolean?
    ) {
        binding.apply {
            btnPositive.setOnClickListener {
                onClickAccept()
                dismiss()
            }
            btnNegative.setOnClickListener {
                onClickCancel()
            }

            if (title.isNullOrEmpty()) {
                mTitle.gone()
            } else {
                mTitle.text = title
            }

            if (description.isNullOrEmpty()) {
                mDescription.gone()
            } else {
                mDescription.text = description
            }

            if (!textBtnPositive.isNullOrEmpty()) {
                btnPositive.text = textBtnPositive
            }
            if (!textBtnNegative.isNullOrEmpty()) {
                btnNegative.text = textBtnNegative
            }

            cancelable?.let { dialog?.setCancelable(it) }

            btnPositive.visibility = if (showBtnPositive) View.VISIBLE else View.GONE
            btnNegative.visibility = if (showBtnNegative) View.VISIBLE else View.GONE

            if (showDrawable == true) {
                ivIconDialog.isGone = false
                ivIconDialog.setImageResource(drawable)
            } else {
                ivIconDialog.isGone = true
            }
        }
    }

    private fun animateEntrance(container: View) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                container.translationY = container.height.toFloat()
                container.alpha = 0f
                container.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(JellyInterpolator())
                    .setDuration(500)
                    .start()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}

