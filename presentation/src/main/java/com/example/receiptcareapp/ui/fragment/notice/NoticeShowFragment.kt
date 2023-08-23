package com.example.receiptcareapp.ui.fragment.notice

import androidx.fragment.app.activityViewModels
import com.example.domain.model.receive.NoticeData
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentNoticeShowBinding
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeShowFragment : BaseFragment<FragmentNoticeShowBinding>(
    FragmentNoticeShowBinding::inflate, "ShowNoticeFragment"
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var viewModelData: NoticeData

    override fun initData() {
        viewModelData = activityViewModel.selectedNoticeData
    }

    override fun initUI() {
        with(binding){
            title = viewModelData.title
            date = viewModelData.date
            content = viewModelData.content
        }
    }

    override fun initListener() {}

    override fun initObserver() {}

}