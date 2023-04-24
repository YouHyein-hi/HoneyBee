package com.example.receiptcareapp.fragment.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding

/**
 * 2023-01-31
 * pureum
 */

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate:Inflate<VB>
) : Fragment() {

    init {
        Log.e("TAG", "Basefragment : start", )
        Log.e("TAG", "Basefragment : $inflate: ", )
    }

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        Log.e("TAG", "Basefragment : $_binding: ", )
        return binding.root
    }

    //viewBinding으로 인한 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}