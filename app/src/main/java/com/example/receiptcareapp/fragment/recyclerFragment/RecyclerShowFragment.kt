package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.model.DomainRecyclerData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnetedState
import com.example.receiptcareapp.State.DeleteType
import com.example.receiptcareapp.State.ServerState
import com.example.receiptcareapp.databinding.FragmentRecyclerShowBinding
import com.example.receiptcareapp.dto.DeleteData
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.time.LocalDateTime

class RecyclerShowFragment : BaseFragment<FragmentRecyclerShowBinding>(FragmentRecyclerShowBinding::inflate) {
    init {
        Log.e("TAG", ": RecyclerShowFragment Start", )
    }
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val activityViewModel : MainViewModel by activityViewModels()
    private var myData : DeleteData = DeleteData(DeleteType.NONE, DomainRecyclerData("","","","",""))
    private lateinit var callback:OnBackPressedCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myData.data = fragmentViewModel.showLocalData.value
        activityViewModel.changeConnectedState(ConnetedState.DISCONNECTED)
        activityViewModel.changeServerState(ServerState.NONE)

        //서버 데이터일 시
        if(fragmentViewModel.showServerData.value != null){
            myData.type = DeleteType.SERVER
            binding.changeBtn.isVisible = false
            binding.removeBtn.setImageResource(R.drawable.delete_btn)
            binding.changeBtn.setOnClickListener{
                deleteServerAlertDialog()
            }
            // 로컬 데이터 일 시
        }else if(fragmentViewModel.showLocalData.value != null){
            myData.type = DeleteType.LOCAL
            binding.changeBtn.setOnClickListener{
                replayServerAlertDialog()
            }
        }else{
            binding.backgroundText.text = "데이터가 없어요!"
            Toast.makeText(requireContext(), "데이터가 없어요!", Toast.LENGTH_SHORT).show()
//            findNavController().navigate(R.id.action_recyclerShowFragment_to_recyclerFragment)
            findNavController().popBackStack()
        }

        val newDate = myData.data!!.date.split("-","T",":")
        binding.pictureName.text = myData.data!!.pictureName
        binding.imageView.setImageURI(myData.data!!.picture.toUri())
        binding.date.text = "${newDate[0]}.${newDate[1]}.${newDate[2]} / ${newDate[3]}:${newDate[4]}:${newDate[5]}"
        binding.cardAmount.text = "${myData.data!!.cardName} : ${myData.data!!.amount}"



        //삭제 버튼
        binding.removeBtn.setOnClickListener{
            deleteLocalAlertDialog()
        }

        activityViewModel.serverState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated@@@@: ${activityViewModel.serverState.value}", )
            if(it==ServerState.SUCCESS) {
                Toast.makeText(requireContext(), "전송 완료!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            else if(it==ServerState.FALSE){
                Toast.makeText(requireContext(), "전송 실패..", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }



        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
            if(it==ConnetedState.DISCONNECTED) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            else binding.progressBar.visibility = View.VISIBLE
        }


        //뒤로가기 아이콘
        binding.imageBack.setOnClickListener{
            if (activityViewModel.connectedState.value == ConnetedState.CONNECTING) {
                activityViewModel.serverCoroutineStop()
                findNavController().popBackStack()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    //로컬 데이터 재전송 아트다이알로그
    private fun replayServerAlertDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("서버에 보내시겠어요?")
            .setPositiveButton("닫기"){dialog, id-> }
            .setNegativeButton("보내기"){dialog, id->
                //서버와 연결!
                val data = myData.data
                Log.e("TAG", "onViewCreated: ${myData.data!!.date}", )
                val myDate = myData.data!!.date.split("-","T",":")
                if (data != null) {
                    activityViewModel.sendData(
                        date = LocalDateTime.of(myDate[0].toInt(), myDate[1].toInt(), myDate[2].toInt(), myDate[3].toInt(), myDate[4].toInt(), myDate[5].toInt()),
                        amount = data.amount,
                        cardName = data.cardName,
                        file = data.picture.toUri(),
                        storeName = data.pictureName
                    )
                }else{
                    Log.e("TAG", "onViewCreated: 전송 실패..", )
                }
            }.create().show()
    }

    //서버 삭제 아트다이알로그
    private fun deleteServerAlertDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("서버 데이터를 삭제하시겠어요?")
            .setPositiveButton("닫기"){dialog, id->

            }
            .setNegativeButton("삭제하기"){dialog, id->
                //서버와 연결!
                activityViewModel.changeConnectedState(ConnetedState.CONNECTING)
                val data = myData.data
                Log.e("TAG", "onViewCreated: ${myData.data!!.date}", )
                val myDate = myData.data!!.date.split("-","T",":")
                if (data != null) {
                    activityViewModel.deleteServerData(
                        LocalDateTime.of(myDate[0].toInt(), myDate[1].toInt(), myDate[2].toInt(), myDate[3].toInt(), myDate[4].toInt(), myDate[5].toInt()).toString()
                    )
                    findNavController().popBackStack()
                }else{
                    Log.e("TAG", "onViewCreated: 전송 실패..", )
                }
            }.create().show()
    }

    //로컬 삭제 아트다이알로그
    private fun deleteLocalAlertDialog(){
        AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("정말 삭제하실 건가요?\n삭제한 데이터는 복구시킬 수 없어요.")
            .setPositiveButton("닫기"){dialog, id->

            }
            .setNegativeButton("삭제"){dialog, id->
                activityViewModel.deleteRoomData(myData.data!!.date)
                findNavController().popBackStack()
            }.create().show()
    }

    /** Fragment 뒤로가기 **/
    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activityViewModel.connectedState.value == ConnetedState.CONNECTING) {
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