package com.example.receiptcareapp.ui.fragment.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentNoticeShowBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeShowFragment : BaseFragment<FragmentNoticeShowBinding>(
    FragmentNoticeShowBinding::inflate, "ShowNoticeFragment"
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModelData = activityViewModel.selectedNoticeData

    override fun initData() {

    }

    override fun initUI() {
        with(binding){
            title = viewModelData.title
            date = viewModelData.date.toString()
            content = viewModelData.content
        }
    }

    override fun initListener() {}

    override fun initObserver() {}

}