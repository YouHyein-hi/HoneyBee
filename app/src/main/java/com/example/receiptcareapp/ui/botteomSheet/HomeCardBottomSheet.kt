package com.example.receiptcareapp.ui.botteomSheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.data.manager.PreferenceManager
import com.example.domain.model.UpdateCardData
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.base.BaseBottomSheet
import com.example.receiptcareapp.databinding.FragmentHomeCardBottomsheetBinding
import com.example.receiptcareapp.ui.activity.LoginActivity
import com.example.receiptcareapp.ui.dialog.CardAddDialog_Bottom
import com.example.receiptcareapp.ui.adapter.HomeCardAdapter
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.CardAddBottomViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * 2023-03-22
 * pureum
 */
class HomeCardBottomSheet: BaseBottomSheet<FragmentHomeCardBottomsheetBinding>(
    FragmentHomeCardBottomsheetBinding::inflate,
    "homeCardBottomSheet"
) {
    private val adapter: HomeCardAdapter = HomeCardAdapter()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val homeCardAddBottomViewModel : HomeCardBottomSheetViewModel by viewModels()
    private var uid : Long = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun initData() {
        //initData 부분
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
        //서버 데이터 불러오기
        activityViewModel.receiveServerCardData()
    }

    override fun initUI() {
        //initUI 부분
        binding.cardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecyclerview.adapter = adapter
    }

    override fun initListener() {
        //서버 카드 추가 다이얼로그
        binding.addBtn.setOnClickListener{
            val cardAddDialogBottom = CardAddDialog_Bottom()
            cardAddDialogBottom.show(parentFragmentManager, "CardAddDialog")
        }

        //서버 카드 수정 및 삭제 다이알로그
        adapter.onLocalSaveClic = {
            val dialogView = layoutInflater.inflate(R.layout.dialog_server_send_card, null)
            val cardName  = dialogView.findViewById<TextView>(R.id.dialog_server_cardName)
            val cardPrice = dialogView.findViewById<EditText>(R.id.dialog_server_cardPrice)

            cardName.text = "${it.cardName}"

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
                    Log.e("TAG", "onCreateView: 수정 버튼 누름", )
                    Log.e("TAG", "cardName.text.toString() : ${cardName.text}", )
                    Log.e("TAG", "adapter.dataList: ${adapter.dataList}", )
                    Log.e("TAG", "onCreateView uid: $uid", )
                    var price = homeCardAddBottomViewModel.CommaReplaceSpace(cardPrice.text.toString())
                    activityViewModel.updateCardData(
                        updateCardData = UpdateCardData(
                            id = uid,
                            cardName = cardName.text.toString(),
                            cardAmount = price.toInt()
                        )
                    )
                }
                .setNegativeButton("취소") { dialog, id -> }
                .show()

            gap.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            gap.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)
        }
    }

    override fun initObserver() {
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            when(it){
                ConnectedState.CONNECTING -> {
                    binding.connectingView.isVisible = true
                    binding.serverProgressBar.isVisible = true
                    binding.addBtn.isClickable = false
                    setCenterText("", false)
                }
                ConnectedState.DISCONNECTED -> {
                    binding.connectingView.isVisible = false
                    binding.serverProgressBar.isVisible = false
                    binding.addBtn.isClickable = true
                    setCenterText("", false)
                }
                ConnectedState.CONNECTING_SUCCESS -> {
                    Toast.makeText(requireContext(), "전송 성공!", Toast.LENGTH_SHORT).show()
                    binding.connectingView.isVisible = false
                    binding.serverProgressBar.isVisible = false
                    binding.addBtn.isClickable = true
                    setCenterText("", false)
                }
                ConnectedState.CONNECTING_FALSE -> {
                    binding.connectingView.isVisible = false
                    binding.serverProgressBar.isVisible = false
                    binding.addBtn.isClickable = true
                    setCenterText("", false)
                    setCenterText("서버와 연결 실패!", true)
                }
                else ->{}
            }
            Log.e("TAG", "onCreateView: $it", )
        }

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