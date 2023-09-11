package com.example.receiptcareapp.ui.fragment.notice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.remote.receive.notice.NoticeData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.databinding.FragmentNoticeBinding
import com.example.receiptcareapp.ui.adapter.NoticeAdapter
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.notice.NoticeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment : BaseFragment<FragmentNoticeBinding>(
    FragmentNoticeBinding::inflate,"NoticeFragment"
) {
    private val viewModel: NoticeViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val adapter: NoticeAdapter = NoticeAdapter()
    override fun initData() {}

    override fun initUI() {
        viewModel.getNoticeList()
        binding.noticeListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.noticeListRecyclerview.adapter = adapter
    }

    override fun initListener() {
        binding.baseComponent.title.setOnLongClickListener {
            findNavController().navigate(R.id.action_noticeFragment_to_addNoticeFragment)
            true
        }
        binding.baseComponent.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        adapter.onNoticeClic = {
            activityViewModel.selectedNoticeData = it
            findNavController().navigate(R.id.action_noticeFragment_to_showNoticeFragment, )
        }
        binding.noticeListRecyclerviewRefresh.setOnRefreshListener {
            binding.noticeListRecyclerviewRefresh.isRefreshing = false
            viewModel.getNoticeList()
        }
    }

    override fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner){
            adapter.dataList.clear()
            adapter.dataList = it?.body!!.toMutableList()
        }

        //TODO 프로그레스바 databinding
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding. layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
            checkDataList()
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }


    private fun checkDataList(){
        if(adapter.dataList.isEmpty()) binding.noticeEmptyTxt.visibility = View.VISIBLE
        else  binding.noticeEmptyTxt.visibility = View.INVISIBLE
    }


}