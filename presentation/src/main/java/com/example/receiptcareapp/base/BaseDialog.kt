package com.example.receiptcareapp.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding


abstract class BaseDialog<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : DialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 0.85).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        initListener()
        initObserver()
    }

    // binding 으로 인한 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    abstract fun initData()
    abstract fun initUI()
    abstract fun initListener()
    abstract fun initObserver()

    fun showShortToast(message: String?){
        context?.let {
            Toast.makeText(it, message ?: "", Toast.LENGTH_SHORT).show()
        }
    }

    fun showLongToast(message: String?){
        context?.let {
            Toast.makeText(it, message ?: "", Toast.LENGTH_LONG).show()
        }
    }
}