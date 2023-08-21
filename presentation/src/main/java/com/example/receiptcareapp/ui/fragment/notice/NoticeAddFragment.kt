package com.example.receiptcareapp.ui.fragment.notice

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.model.send.DomainAddNoticeData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentNoticeAddBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.notice.NoticeAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class NoticeAddFragment : BaseFragment<FragmentNoticeAddBinding>(
    FragmentNoticeAddBinding::inflate, "NoticeAddUpdateFragment"
) {
    private val viewModel: NoticeAddViewModel by viewModels()

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        binding.addBtn.setOnClickListener {
            viewModel.insertNotice(
                DomainAddNoticeData(
                    title = binding.titleEditTxt.text.toString(),
                    date = LocalDateTime.now(),
                    content = binding.contentEditTxt.text.toString()
                )
            )
        }

        binding.baseComponent.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initObserver() {
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.response.observe(viewLifecycleOwner){
            when(it?.status){
                "200" -> {
                    showShortToast("전송 성공!")
                    findNavController().popBackStack()
                }
                else -> showShortToast("전송 실패..")
            }
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

}