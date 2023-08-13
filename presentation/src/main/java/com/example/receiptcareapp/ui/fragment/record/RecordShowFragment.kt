package com.example.receiptcareapp.ui.fragment.record

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.ui.dialog.ChangeDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordShowBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.dialog.DeleteDialog
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.viewModel.fragmentViewModel.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordShowFragment : BaseFragment<FragmentRecordShowBinding>(FragmentRecordShowBinding::inflate, "RecordShowFragment") {
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private val viewModel : RecordShowViewModel by viewModels()
    private lateinit var viewModelData: RecyclerData
    private lateinit var callback:OnBackPressedCallback

    init {
        Log.e("TAG", "RecyclerShowFragment RecyclerShowFragment RecyclerShowFragment: ", )
    }

    override fun initData() {
        if(activityViewModel.selectedData.value != null) viewModelData = activityViewModel.selectedData.value!!
        else findNavController().popBackStack()
    }

    override fun initUI() {
        // TODO 재전송 버튼은 일단 비활성화
        initView()
        if(binding.imageView.drawable == null) binding.emptyText.isVisible
    }

    override fun initListener() {
        //수정 후 재전송 버튼
        binding.changeBtn.setOnClickListener{ changeDialog() }
        //삭제 버튼
        binding.removeBtn.setOnClickListener{ deleteDialog() }
        //뒤로가기 버튼
        binding.backBtn.setOnClickListener{ findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment) }
    }

    override fun initObserver() {
        //Todo api 요청에서 ViewModel 전부 State 처리해야함
        viewModel.response.observe(viewLifecycleOwner){
            when(it){
                ResponseState.UPDATE_SUCCESS -> {
                    showShortToast("수정 완료!")
                    findNavController().popBackStack()
                }
                ResponseState.DELETE_SUCCESS -> {
                    showShortToast("삭제 완료!")
                    findNavController().popBackStack()
                }
                else -> {
                    showShortToast("전송 실패...")
                    findNavController().popBackStack()
                }
            }
        }

        //서버 연결 상태 옵져버
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.picture.observe(viewLifecycleOwner){
            Glide.with(binding.imageView)
                .load(it)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(binding.imageView)
        }
    }

    private fun initView(){
        if(viewModelData.type == ShowType.LOCAL) insertLocalPicture(viewModelData.file.toString())
        else viewModel.getServerPictureData(viewModelData.uid)
        binding.imageView.clipToOutline = true
        binding.cardTxt.text = viewModelData.cardName
        binding.dateTxt.text = viewModelData.billSubmitTime
        binding.amountTxt.text = viewModelData.amount
        binding.cardAmount.text = "${viewModelData.cardName}카드 : ${viewModelData.amount}원"
    }

    private fun insertLocalPicture(insert: String){
        binding.imageView.setImageURI(insert.toUri())
    }

    //수정
    private fun changeDialog(){
        val changeDialog = ChangeDialog(viewModel)
        changeDialog.show(parentFragmentManager, "changeDialog")
    }

    //서버와 로컬 삭제
    private fun deleteDialog(){
       val deleteDialog = DeleteDialog()
        deleteDialog.show(parentFragmentManager, "deleteDialog")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: show destroy", )
    }

    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}