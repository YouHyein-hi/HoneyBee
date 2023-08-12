package com.example.receiptcareapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.databinding.FragmentNoticeBinding

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(
    FragmentNoticeBinding::inflate,"NoticeFragment"
) {
    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {
        binding.baseComponent.title.setOnLongClickListener {
            findNavController().navigate(R.id.action_noticeFragment_to_addNoticeFragment)
            true
        }
    }

    override fun initObserver() {
    }


}