package com.example.receiptcareapp.ui.botteomSheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.FragmentHomeCardBottomsheetBinding
import com.example.receiptcareapp.ui.dialog.CardAddDialog
import com.example.receiptcareapp.ui.adapter.HomeCardAdapter
import com.example.receiptcareapp.util.ResponseState
import com.example.receiptcareapp.ui.dialog.CardChangeDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-03-22
 * pureum
 */
@AndroidEntryPoint
class HomeCardBottomSheet: BaseBottomSheet<FragmentHomeCardBottomsheetBinding>(
    FragmentHomeCardBottomsheetBinding::inflate,
    "homeCardBottomSheet"
) {
    private val adapter: HomeCardAdapter = HomeCardAdapter()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel : HomeCardViewModel by viewModels()
    private var uid : Long = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
        //initData 부분
//        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
        //서버 데이터 불러오기
        viewModel.getServerCardData()
    }

    override fun initUI() {
        binding.cardCount = "0"
        Log.e("TAG", "initUI homecardBottomSheet: ${binding.cardCount}")
        //initUI 부분
        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter
    }

    override fun initListener() {
        //서버 카드 추가 다이얼로그
        binding.cardCount = "0"
        binding.addBtn.setOnClickListener{
            val cardAddDialog = CardAddDialog()
            cardAddDialog.show(parentFragmentManager, "CardAddDialog")
        }

        //서버 카드 수정 및 삭제 다이알로그
        adapter.onLocalSaveClic = {
            val cardChangeDialog = CardChangeDialog(it)
            cardChangeDialog.show(parentFragmentManager, "CardChangeDialog")
        }



    }

    override fun initObserver() {
        binding.cardCount = "0"

        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.response.observe(viewLifecycleOwner){
            when(it){
                ResponseState.SUCCESS -> {
                    setCenterText("추가 성공!", true)
                }
                else -> {
                    setCenterText("전송 실패!", true)
                }
            }
        }

//        activityViewModel.connectedState.observe(viewLifecycleOwner){
//            when(it){
//                ConnectedState.CONNECTING -> {
//                    binding.connectingView.isVisible = true
//                    binding.serverProgressBar.isVisible = true
//                    binding.addBtn.isClickable = false
//                    setCenterText("", false)
//                }
//                ConnectedState.DISCONNECTED -> {
//                    binding.connectingView.isVisible = false
//                    binding.serverProgressBar.isVisible = false
//                    binding.addBtn.isClickable = true
//                    setCenterText("", false)
//                }
//                ConnectedState.CONNECTING_SUCCESS -> {
//                    Toast.makeText(requireContext(), "전송 성공!", Toast.LENGTH_SHORT).show()
//                    binding.connectingView.isVisible = false
//                    binding.serverProgressBar.isVisible = false
//                    binding.addBtn.isClickable = true
//                    setCenterText("", false)
//                }
//                ConnectedState.CONNECTING_FALSE -> {
//                    binding.connectingView.isVisible = false
//                    binding.serverProgressBar.isVisible = false
//                    binding.addBtn.isClickable = true
//                    setCenterText("", false)
//                    setCenterText("서버와 연결 실패!", true)
//                }
//                else ->{}
//            }
//            Log.e("TAG", "onCreateView: $it", )
//        }

        activityViewModel.cardData.observe(viewLifecycleOwner) { dataList ->
            if (dataList.isEmpty()) { setCenterText("데이터가 비었어요!", true)
            } else {
                Log.e("TAG", "onCreateView: dataList  ${dataList}", )
                for (data in dataList) {
                    Log.e("TAG", "onCreateView: data  ${data}", )
                    uid = data.uid
                    // uid 값을 사용하여 원하는 작업 수행
                    Log.e("TAG", "uid: $uid")
                }
                setCenterText("", false)
                adapter.dataList = dataList
            }
        }
    }

    // 창 내려갈때 서버 통신 시 중지
    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.serverCoroutineStop()
    }

    fun setCenterText(text:String, state:Boolean){
        binding.centerStateTxt.text = text
        binding.centerStateTxt.isVisible = state
    }


}