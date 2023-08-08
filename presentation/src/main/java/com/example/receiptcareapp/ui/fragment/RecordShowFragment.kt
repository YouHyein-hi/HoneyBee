package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.dto.ShowData
import com.example.receiptcareapp.ui.dialog.ChangeDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordShowBinding
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.viewModel.fragmentViewModel.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordShowFragment(
) : BaseFragment<FragmentRecordShowBinding>(FragmentRecordShowBinding::inflate, "RecordShowFragment") {
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private val viewModel : RecordShowViewModel by viewModels()
    private lateinit var myData: ShowData
    private lateinit var callback:OnBackPressedCallback

    init {
        Log.e("TAG", "RecyclerShowFragment RecyclerShowFragment RecyclerShowFragment: ", )
    }

    override fun initData() {
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
    }

    override fun initUI() {
        if(activityViewModel.selectedData.value != null){
            binding.resendBtn.isVisible = false
            val data = activityViewModel.selectedData.value
            myData = ShowData(data!!.type, data.uid, data.cardName, data.amount, data.billSubmitTime, data.storeName, data.file)
            binding.imageView.setImageURI(myData.file)
            binding.pictureName.text = myData.storeName
            binding.imageView.clipToOutline = true
            binding.date.text = myData.billSubmitTime
            binding.cardAmount.text = "${myData.cardName}카드 : ${myData.amount}원"
        }else{
            binding.emptyText.text = "데이터가 없어요!"
            showShortToast("데이터가 없어요!")
            findNavController().popBackStack()
        }
    }

    override fun initListener() {
        //재전송 버튼 / 삭제기능
//        binding.resendBtn.setOnClickListener{ resendDialog() }
        //수정 후 재전송 버튼
        binding.changeBtn.setOnClickListener{ changeDialog() }
        //삭제 버튼
        binding.removeBtn.setOnClickListener{ deleteDialog() }
        //뒤로가기 버튼
        binding.backBtn.setOnClickListener{
            if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
                activityViewModel.serverCoroutineStop()
                findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment)
            } else {
                findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment)
            }
        }
    }

    override fun initObserver() {
        //서버 연결 상태 옵져버
//        viewModel.loading.observe(viewLifecycleOwner){
//            Log.e("TAG", "show onViewCreated: $it", )
//            if(it==ConnectedState.DISCONNECTED) {
//                binding.progressBar.visibility = View.INVISIBLE
//            }
//            else if(it==ConnectedState.CONNECTING){
//                binding.progressBar.visibility = View.VISIBLE
//            }
//            else if(it==ConnectedState.CONNECTING_SUCCESS){
//                showShortToast("전송 완료!")
//                findNavController().popBackStack()
//            }else{
//                showShortToast("전송 실패...")
//                findNavController().popBackStack()
//            }
//        }
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
    }

    //서버, 로컬 재전송
    private fun resendDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("서버에 보내시겠어요?")
            .setNegativeButton("보내기"){dialog,id->
                dialog.dismiss()
                resendData()
            }
            .setPositiveButton("닫기"){dialog,id->
                dialog.dismiss()
            }
            .create().show()
    }
    //로컬재전송
    private fun resendData(){
//        activityViewModel.resendData(
//            AppSendData(myData.cardName,myData.amount, myData.billSubmitTime, myData.storeName,myData.file!!),
//            myData.billSubmitTime
//        )
    }

    //수정
    private fun changeDialog(){
//        activityViewModel.changeConnectedState(ConnectedState.CONNECTING)
        val changeDialogFragment = ChangeDialog()
        changeDialogFragment.show(parentFragmentManager, "CustomDialog")
    }

    //서버와 로컬 삭제
    private fun deleteDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("정말 삭제하실 건가요?\n삭제한 데이터는 복구시킬 수 없어요.")
            .setNegativeButton("삭제하기"){dialog,id->
                Log.e("TAG", "deleteDialog: ${myData}", )
                if(myData.type == ShowType.SERVER){
                    viewModel.deleteServerBillData(myData.uid.toLong())
                }else{
                    viewModel.deleteRoomBillData(myData.billSubmitTime)
                    findNavController().popBackStack()
                }
            }
            .setPositiveButton("닫기"){dialog,id->
                dialog.dismiss()
            }
            .create().show()
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
                if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
                    activityViewModel.serverCoroutineStop()
                } else {
                    findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}