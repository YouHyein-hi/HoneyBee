package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.databinding.FragmentRecyclerShowBinding
import com.example.receiptcareapp.dto.ShowData
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel

class RecyclerShowFragment : BaseFragment<FragmentRecyclerShowBinding>(FragmentRecyclerShowBinding::inflate) {
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val activityViewModel : MainViewModel by activityViewModels()
    private lateinit var myData: ShowData
    private lateinit var callback:OnBackPressedCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
//        activityViewModel.changeServerState(ServerState.NONE)

        // 서버, 로컬 데이터를 구분하여 맞춰 아트다이알로그를 띄움
        //서버 데이터일 시
        if(fragmentViewModel.showServerData.value != null){
            binding.resendBtn.isVisible = false
            val data = fragmentViewModel.showServerData.value
            myData = ShowData(ShowType.SERVER, data!!.uid, data.cardName, data.amount, data.date, data.storeName, data.file)

        // 로컬 데이터 일 시
        }else if(fragmentViewModel.showLocalData.value != null){
            val data = fragmentViewModel.showLocalData.value
            var bitmap = BitmapFactory.decodeFile(data?.file?.path)
            myData = ShowData(ShowType.LOCAL, data!!.uid, data.cardName, data.amount, data.date, data.storeName, bitmap)
        }else{
            binding.backgroundText.text = "데이터가 없어요!"
            Toast.makeText(requireContext(), "데이터가 없어요!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.pictureName.text = myData.storeName
        binding.imageView.setImageBitmap(myData.file)
        binding.imageView.clipToOutline = true
        binding.date.text = myData.date
        binding.cardAmount.text = "${myData.cardName}카드 : ${myData.amount}원"


        //재전송 버튼, 서버와 로컬
        binding.resendBtn.setOnClickListener{
            resendDialog()
        }
        //수정 버튼, 서버
        binding.changeBtn.setOnClickListener{
            changeDialog()
        }
        //삭제 버튼, 서버와 로컬
        binding.removeBtn.setOnClickListener{
            deleteDialog()
        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
            if(it==ConnectedState.DISCONNECTED) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            else if(it==ConnectedState.DISCONNECTED){
                binding.progressBar.visibility = View.VISIBLE
            }
            else if(it==ConnectedState.CONNECTING_SUCCESS){
                Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), "전송 실패..", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }


        //뒤로가기 아이콘
        binding.backBtn.setOnClickListener{
            if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
                activityViewModel.serverCoroutineStop()
                findNavController().popBackStack()
            } else {
                findNavController().popBackStack()
            }
        }
    }
    //서버, 로컬 재전송
    private fun resendDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("서버에 보내시겠어요?")
            .setPositiveButton("닫기"){_,_->}
            .setNegativeButton("보내기"){_,_->
//                    activityViewModel.sendData(
//                        SendData(id = myData.id, cardName = myData.cardName, amount = myData.date, date = myData.date, picture = myData.picture, storeName = myData.pictureName))
            }
            .create().show()
    }

    //서버 수정
    private fun changeDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("데이터를 수정하시겠어요?")
            .setPositiveButton("닫기"){_,_->findNavController().popBackStack()}
            .setNegativeButton("수정해서 보내기"){_,_->
//                activityViewModel.changeServerData(SendData(
//                    id = myData.id, cardName = myData.cardName, amount = myData.date, date = myData.date, picture = myData.picture, storeName = myData.pictureName))
                findNavController().popBackStack()
            }
            .create().show()
    }
    //서버와 로컬 삭제
    private fun deleteDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("정말 삭제하실 건가요?\n삭제한 데이터는 복구시킬 수 없어요.")
            .setPositiveButton("닫기"){_,_->findNavController().popBackStack()}
            .setNegativeButton("삭제하기"){_,_->
                Log.e("TAG", "deleteDialog: ${myData}", )
                if(myData.type == ShowType.SERVER){
                    activityViewModel.deleteServerData(myData.uid.toLong())
                }else{
                    activityViewModel.deleteRoomData(myData.date)
                }
                findNavController().popBackStack()
            }
            .create().show()
    }

    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
                    activityViewModel.serverCoroutineStop()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

//    private fun myBack(){
//        activityViewModel.serverCoroutineStop()
//        findNavController().popBackStack()
//    }
}