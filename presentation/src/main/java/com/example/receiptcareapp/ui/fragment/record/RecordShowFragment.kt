package com.example.receiptcareapp.ui.fragment.record

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.ui.dialog.ChangeDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordShowBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.dialog.DeleteDialog
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


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
        binding.recordChangeBtn.setOnClickListener{ changeDialog() }
        //삭제 버튼
        binding.recoreRemoveBtn.setOnClickListener{ deleteDialog() }
        //뒤로가기 버튼
        binding.recordBackBtn.setOnClickListener{ findNavController().popBackStack() }

        binding.recordDownloadBtn.setOnClickListener{ downloadImage(viewModelData.file.toString()) }
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
        //TODO 데이터바인딩
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        //TODO 데이터 바인딩
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
        if (viewModelData.type == ShowType.LOCAL)
            binding.recoreImageView.setImageURI(viewModelData.file)
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
        if(binding.recoreImageView.drawable == null)
            binding.recordEmptyTxt.isVisible = true
        if(viewModel.picture.value==null)
            binding.recordEmptyTxt.isVisible = true
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


    private fun downloadImage(url : String) {
        val fileName =
                "/${getString(R.string.app_name)}/${SimpleDateFormat("yyyyMMddHHmmss").format(Date())}.jpg" // 이미지 파일 명

            val req = DownloadManager.Request(Uri.parse(url))

            req.setTitle(fileName) // 제목
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // 알림 설정
                .setMimeType("image/*")
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    fileName
                ) // 다운로드 완료 시 보여지는 이름

        val manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        manager.enqueue(req)
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
/*

 */