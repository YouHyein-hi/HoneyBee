package com.example.receiptcareapp.ui.fragment.record

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
    private val billCheckCancelDialog: BillCheckCancelDialog by lazy { BillCheckCancelDialog(viewModel) }
    private val billCheckCompleteDialog: BillCheckCompleteDialog by lazy { BillCheckCompleteDialog(viewModel) }
    private val changeDialog: ChangeDialog by lazy { ChangeDialog(viewModel) }
    private val deleteDialog: DeleteDialog by lazy { DeleteDialog(viewModel) }
    private val downLoadDialog: DownLoadDialog by lazy { DownLoadDialog(viewModel) }

    init {
        Log.e("TAG", "RecyclerShowFragment RecyclerShowFragment RecyclerShowFragment: ", )
    }

    override fun initData() {
        if(activityViewModel.selectedData.value != null)
            viewModelData = activityViewModel.selectedData.value!!
        else
            findNavController().popBackStack()
    }

    override fun initUI() {
        initView()
    }

    override fun initListener() {
        // 이미지 다운로드 버튼
        binding.recordDownloadBtn.setOnClickListener{ downloadDialog() }
        //수정 후 재전송 버튼
        binding.recordChangeBtn.setOnClickListener{
            viewModel.picture.value?.let { it -> viewModel.takeChangePicture(it) }
            viewModelData.file?.let { it -> viewModel.takeImage(it) }
            Log.e("TAG", "initListener: ${viewModel.picture.value}", )
            Log.e("TAG", "initListener: ${viewModel.image.value}", )
            changeDialog()
        }
        //삭제 버튼
        binding.recoreRemoveBtn.setOnClickListener{ deleteDialog() }
        //뒤로가기 버튼
        binding.recordBackBtn.setOnClickListener{ findNavController().popBackStack() }
        //청구버튼
        binding.billCheckBtn.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                false -> {
                    showBillCheckCancelDialog()
                    //binding.billCheckBtn.isChecked = false
                    //통신 실패할 경우 원래대로 처리해야함
                }
                true -> {
                    showBillCheckCompleteDialog()
                    //binding.billCheckBtn.isChecked = true
                    //통신 실패할 경우 원래대로 처리해야함
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

        viewModel.picture.observe(viewLifecycleOwner){
            Glide.with(binding.recoreImageView)
                .load(it)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(binding.recoreImageView)
            checkImageData()
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun initView() {
        Log.e("TAG", "initView: $viewModelData",)
        if (viewModelData.type == ShowType.LOCAL){
            binding.recoreImageView.setImageURI(viewModelData.file)
            binding.recordDownloadBtn.isVisible = false
        }
        else
            viewModel.getServerPictureData(viewModelData.uid)

//        checkImageData()
        binding.recoreImageView.clipToOutline = true
        binding.data = RecyclerData(
            viewModelData.type, viewModelData.uid, viewModelData.cardName, viewModelData.amount,
            StringUtil.changeDate(viewModelData.date), viewModelData.storeName, viewModelData.file
        )
    }

    private fun checkImageData(){
//        if(binding.recoreImageView.drawable == null)
//            binding.recordEmptyTxt.isVisible = true
//        if(viewModel.picture.value==null)
//            binding.recordEmptyTxt.isVisible = true
    }

    //수정
    private fun changeDialog() {
        changeDialog.show(parentFragmentManager, "changeDialog")
    }

    //서버와 로컬 삭제
    private fun deleteDialog() {
        deleteDialog.show(parentFragmentManager, "deleteDialog")
    }

    //이미지 다운로드
    private fun downloadDialog(){
        downLoadDialog.show(parentFragmentManager, "downLoadDialog")
    }

    private fun showBillCheckCancelDialog() = billCheckCancelDialog.show(parentFragmentManager,"billCheckCancelDialog")

    private fun showBillCheckCompleteDialog() = billCheckCompleteDialog.show(parentFragmentManager,"billCheckCompleteDialog")

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: show destroy", )
    }

    /** Fragment 뒤로가기 **/
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