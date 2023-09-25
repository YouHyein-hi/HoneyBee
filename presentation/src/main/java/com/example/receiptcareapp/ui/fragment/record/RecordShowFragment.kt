package com.example.receiptcareapp.ui.fragment.record

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.remote.receive.bill.DetailBillData
import com.example.domain.util.StringUtil
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordShowBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.state.ResponseState
import com.example.receiptcareapp.ui.dialog.*
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordShowFragment : BaseFragment<FragmentRecordShowBinding>(FragmentRecordShowBinding::inflate, "RecordShowFragment") {
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private val viewModel : RecordShowViewModel by viewModels()
    private lateinit var viewModelData: RecyclerData
    private lateinit var callback:OnBackPressedCallback
    private val billCheckDialog: BillCheckDialog by lazy { BillCheckDialog(viewModel, viewModelData.uid) }
    private val changeDialog: ChangeDialog by lazy { ChangeDialog(viewModel) }
    private val deleteDialog: DeleteDialog by lazy { DeleteDialog(viewModel) }
    private val downLoadDialog: DownLoadDialog by lazy { DownLoadDialog(viewModel) }

    override fun initData() {
        viewModelData = activityViewModel.selectedData.value!!
    }

    override fun initUI() {
        binding.recoreImageView.clipToOutline = true
        if (viewModelData.type == ShowType.LOCAL){
            binding.isServer = false
            binding.recoreImageView.setImageURI(viewModelData.file)
            binding.recordDownloadBtn.isVisible = false
            initContext()
        }
        else {
            binding.isServer = true
            viewModel.getServerInitData(viewModelData.uid)
            binding.billCheckBtn.isChecked = activityViewModel.check.value!!
        }
    }

    override fun initListener() {
        binding.recordDownloadBtn.setOnClickListener{ downloadDialog() }

        binding.recordChangeBtn.setOnClickListener{
            viewModel.serverInitData.value?.let { it -> viewModel.takeChangePicture(it.first!!) }
            viewModelData.file?.let { it -> viewModel.takeImage(it) }
            activityViewModel.changeSelectedData(
                RecyclerData(
                    type = ShowType.SERVER,
                    uid = viewModelData.uid,
                    cardName = viewModelData.cardName,
                    amount = viewModelData.amount,
                    date = viewModelData.date,
                    storeName = viewModelData.storeName,
                    file = null,
                    memo = binding.recordShowMemo.text.toString()
                )
            )
            changeDialog()
        }

        binding.recoreRemoveBtn.setOnClickListener{ deleteDialog() }

        binding.recordBackBtn.setOnClickListener{ findNavController().popBackStack() }

        binding.billCheckBtn.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                false -> {
                    billCheckDialog()
                    binding.billCheckBtn.isChecked = false
                }
                true -> {
                    billCheckDialog()
                    binding.billCheckBtn.isChecked = true
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner){
            when(it.second?.status){
                "200" -> {
                    when(it.first){
                        ResponseState.DELETE_SUCCESS -> {
                            showShortToast("삭제 완료!")
                            findNavController().popBackStack()
                        }
                        ResponseState.UPDATE_SUCCESS-> {
                            showShortToast("업데이트 완료!")
                            findNavController().popBackStack()
                        }
                        ResponseState.LOCAL_UPDATE_SUCCESS -> {
                            showShortToast("업데이트 완료!")
                            viewModel.upDataRoomData()
                            findNavController().popBackStack()
                        }
                    }
                }
                else-> showShortToast("실패..")
            }
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.serverInitData.observe(viewLifecycleOwner){
            if (it == null) binding.recordEmptyTxt.visibility = View.VISIBLE
            else{
                Glide.with(binding.recoreImageView)
                    .load(it.first)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(binding.recoreImageView)
            }
            initContext(it.second)
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun initContext(serverData: DetailBillData? =null){
        if(serverData != null) {
            binding.data = serverData
        }else{
            binding.data = DetailBillData(
                billId = "",
                cardName = viewModelData.cardName,
                billAmount = viewModelData.amount,
                billSubmitTime = StringUtil.changeDate(viewModelData.date),
                storeName = viewModelData.storeName,
                billCheck = false,
                billMemo = viewModelData.memo,
                writerName = "",
                writeDateTime = "",
                modifierName = "",
                modifyDateTime = ""
            )
        }
    }

    private fun changeDialog() {
        changeDialog.show(parentFragmentManager, "changeDialog")
    }

    private fun deleteDialog() {
        deleteDialog.show(parentFragmentManager, "deleteDialog")
    }

    private fun downloadDialog(){
        downLoadDialog.show(parentFragmentManager, "downLoadDialog")
    }
    private fun billCheckDialog() = billCheckDialog.show(parentFragmentManager,"billCheckDialog")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}