package com.example.receiptcareapp.fragment.homeFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.receive.DomainReceiveCardData
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.databinding.FragmentHomeCardBottomsheetBinding
import com.example.receiptcareapp.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

/**
 * 2023-03-22
 * pureum
 */
class HomeCardBottomSheet : BottomSheetDialogFragment() {
    private val binding: FragmentHomeCardBottomsheetBinding by lazy {
        FragmentHomeCardBottomsheetBinding.inflate(layoutInflater)
    }
    private val adapter: HomeCardAdapter = HomeCardAdapter()
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var callback : OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val list = mutableListOf<DomainReceiveCardData>(
////            DomainReceiveCardData("나라사랑 카드", 10000),
////            DomainReceiveCardData("선민사랑 카드", 5555)
//        )
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)

        //서버 커넥팅 관리
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onCreateView: $it", )
            if(it == ConnectedState.CONNECTING){
                binding.connectingView.isVisible = true
                binding.serverProgressBar.isVisible = true
                binding.addBtn.isClickable = false
                setCenterText("", false)
            }else if(it == ConnectedState.DISCONNECTED){
                binding.connectingView.isVisible = false
                binding.serverProgressBar.isVisible = false
                binding.addBtn.isClickable = true
                setCenterText("", false)
            }else if(it == ConnectedState.CONNECTING_SUCCESS){
                Toast.makeText(requireContext(), "전송 성공!", Toast.LENGTH_SHORT).show()
                binding.connectingView.isVisible = false
                binding.serverProgressBar.isVisible = false
                binding.addBtn.isClickable = true
                setCenterText("", false)
            }else if(it == ConnectedState.CONNECTING_FALSE){
                binding.connectingView.isVisible = false
                binding.serverProgressBar.isVisible = false
                binding.addBtn.isClickable = true
                setCenterText("", false)
                setCenterText("서버와 연결 실패!", true)
            }
        }

        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter

        //서버 데이터 불러오기
        activityViewModel.receiveServerCardData()
        activityViewModel.cardData.observe(viewLifecycleOwner){
            if(it.isEmpty()) setCenterText("데이터가 비었어요!", true)
            else{
                Log.e("TAG", "bottomsheet observe: $it", )
                setCenterText("", false)
                adapter.dataList = it
            }
        }

        //서버 카드 추가 다이얼로그
        binding.addBtn.setOnClickListener{
            cardAddDialog()
        }

        //서버 카드 수정 및 삭제 다이알로그
        adapter.onLocalSaveClic = {
            val dialogView = layoutInflater.inflate(R.layout.dialog_server_send_card, null)
            val cardName  = dialogView.findViewById<TextView>(R.id.dialog_server_cardName)
            val cardPrice = dialogView.findViewById<EditText>(R.id.dialog_server_cardPrice)

            cardName.text = "\n${it.cardName}"

            cardPrice.setOnClickListener {
                if (cardPrice.text.contains(",")) {
                    cardPrice.setText(cardPrice.text.toString().replace(",", ""))
                    cardPrice.setSelection(cardPrice.text.length)
                }
            }

            cardPrice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && cardPrice.text.isNotEmpty()) {
                    val gap = DecimalFormat("#,###")
                    cardPrice.setText(gap.format(cardPrice.text.toString().toInt()))
                }
                handled
            }

            var gap = AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
                .setTitle("서버 카드 금액 수정")
//                .setMessage("${it.name} 금액을 수정하여 서버에 보내시겠어요?")
                .setView(dialogView)
                .setPositiveButton("보내기") { dialog, id ->
//                    activityViewModel.sendCardData(cardName.text.toString(), cardPrice.text.toString())
                }
                .setNeutralButton("삭제"){dialog, id ->
//                    activityViewModel.deleteCardData()
                }
                .setNegativeButton("닫기") { dialog, id -> }
                .show()

            gap.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            gap.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)
            gap.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setTextColor(Color.BLUE)
        }

        return binding.root
    }

    fun setCenterText(text:String, state:Boolean){
        binding.centerStateTxt.text = text
        binding.centerStateTxt.isVisible = state
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    // 창 내려갈때 서버 통신 시 중지
    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.serverCoroutineStop()
    }

    fun cardAddDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_server_add_card, null)
        val cardName  = dialogView.findViewById<EditText>(R.id.addCardName)
        val cardPrice = dialogView.findViewById<EditText>(R.id.addCardAmount)
        var gap = AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
            .setTitle("서버 카드 데이터 추가")
            .setView(dialogView)
            .setPositiveButton("보내기") { dialog, id ->
                if(cardName.text.toString() == ""){
                    //Toast.makeText(requireContext(), "카드 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "onViewCreated: 카드 이름을 입력해주세요", )
                    dialog.dismiss()
                }
                else if(cardPrice.text.toString() == ""){
                    //Toast.makeText(requireContext(), "초기 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "onViewCreated: 초기을 입력해주세요", )
                    dialog.dismiss()
                }
                else{
                    activityViewModel.sendCardData(AppSendCardData(cardName.text.toString(), cardPrice.text.toString().toInt()))
                }
            }
            .setNegativeButton("닫기") { dialog, id -> }
            .setCancelable(false)
            .show()
        gap.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(Color.RED)
        gap.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.BLACK)
    }
}