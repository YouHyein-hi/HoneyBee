package com.example.receiptcareapp.ui.dialog

import androidx.fragment.app.activityViewModels
import com.example.data.util.UriToBitmapUtil
import com.example.receiptcareapp.base.BaseDialog
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.DialogDownloadBinding
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownLoadDialog(
    private val viewModel:RecordShowViewModel
) : BaseDialog<DialogDownloadBinding>(DialogDownloadBinding::inflate) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var viewModelData: RecyclerData

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()
        }
    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            downloadCancelBtn.setOnClickListener{
                dismiss()
            }
            downloadOkBtn.setOnClickListener{
                handleDownloadClick()
                dismiss()
            }
        }
    }

    override fun initObserver() {
        // Err관리
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> dismiss()
                else -> {}
            }
            showShortToast(FetchStateHandler(it))
        }
    }

    // 이미지 저장
    private fun handleDownloadClick() {
        if(!UriToBitmapUtil.imageExternalSave(requireContext(), viewModel.serverInitData.value?.first, requireContext().getString(
                R.string.app_name))){
            showShortToast("그림 저장을 실패하였습니다") }
        else { showShortToast("그림이 갤러리에 저장되었습니다") }
    }

}