package com.example.receiptcareapp.ui.fragment.record

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.util.changeDate
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.ui.dialog.ChangeDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordShowBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.dialog.DeleteDialog
import com.example.receiptcareapp.util.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.util.RoomState
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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
        if(activityViewModel.selectedData.value != null)
            viewModelData = activityViewModel.selectedData.value!!
        else
            findNavController().popBackStack()
    }

    override fun initUI() {
        initView()
    }

    override fun initListener() {
        //수정 후 재전송 버튼
        binding.changeBtn.setOnClickListener{ changeDialog() }
        //삭제 버튼
        binding.removeBtn.setOnClickListener{ deleteDialog() }
        //뒤로가기 버튼
        binding.backBtn.setOnClickListener{ findNavController().popBackStack() }
    }

    override fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner){
            Log.e("TAG", "initObserver: $it", )
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
            checkImageData()
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun initView() {
        Log.e("TAG", "initView: $viewModelData",)
        if (viewModelData.type == ShowType.LOCAL)
            binding.imageView.setImageURI(viewModelData.file)
        else
            viewModel.getServerPictureData(viewModelData.uid)

        checkImageData()
        binding.imageView.clipToOutline = true
        binding.data = RecyclerData(
            viewModelData.type, viewModelData.uid, viewModelData.cardName, viewModelData.amount,
            changeDate(viewModelData.date), viewModelData.storeName, viewModelData.file
        )
    }

    private fun checkImageData(){
        if(binding.imageView.drawable == null)
            binding.emptyText.isVisible = true
        if(viewModel.picture.value==null)
            binding.emptyText.isVisible =true
    }

    //수정
    private fun changeDialog(){
        val changeDialog = ChangeDialog(viewModel)
        changeDialog.show(parentFragmentManager, "changeDialog")
    }

    //서버와 로컬 삭제
    private fun deleteDialog(){
       val deleteDialog = DeleteDialog(viewModel)
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