package com.example.receiptcareapp.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.domain.model.send.AppSendCardData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.databinding.DialogCardBinding
import com.example.receiptcareapp.ui.fragment.HomeFragment
import com.example.receiptcareapp.ui.fragment.ShowPictureFragment
import com.example.receiptcareapp.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.text.DecimalFormat

class CardAddDialog_ShowPicture : DialogFragment() {

    //viewModels 뷰모델의 객체를 생성주는것?
    private val fragmentViewModel : FragmentViewModel by activityViewModels()
    //엑티비티를 따라가는 뷰모델이니까
    // 엑티비티의 생명주기 따라가는 뷰모델이 뭘까?
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var binding : DialogCardBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCardBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        var newCard = 0
        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 1).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)


        with(binding){
            dialogcardEditCardprice.setOnClickListener {
                if (dialogcardEditCardprice.text.contains(",")) {
                    dialogcardEditCardprice.setText(dialogcardEditCardprice.text.toString().replace(",", ""))
                    dialogcardEditCardprice.setSelection(dialogcardEditCardprice.text.length)
                }
            }
            dialogcardEditCardprice.setOnEditorActionListener { v, actionId, event ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE && dialogcardEditCardprice.text.isNotEmpty()) {
                    val gap = DecimalFormat("#,###")
                    dialogcardEditCardprice.setText(gap.format(dialogcardEditCardprice.text.toString().toInt()))
                }
                handled
            }

            // EditText 비어있을 시 나타나는 style 이벤트
            val hintCardNmae = dialogcardEditCardname.hint
            val emphasis_yellow = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.emphasis_yellow))
            dialogcardEditCardname.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditCardname.text.isEmpty()) {
                    dialogcardEditCardname.hint = "카드 이름을 꼭 적어주세요!"
                    dialogcardEditCardname.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditCardname.text.isEmpty())  {
                    dialogcardEditCardname.hint = hintCardNmae // 초기 hint로 되돌리기
                    dialogcardEditCardname.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditCardname.text.isEmpty()){
                    dialogcardEditCardname.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
            val hintCardPrice = dialogcardEditCardprice.hint
            dialogcardEditCardprice.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && dialogcardEditCardprice.text.isEmpty()) {
                    // 포커스를 가지고 있지 않은 경우 AND Empty인 경우
                    dialogcardEditCardprice.hint = "초기 금액을 꼭 적어주세요!"
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.RED)
                } else if(hasFocus && !dialogcardEditCardprice.text.isEmpty()) {
                    dialogcardEditCardprice.hint = hintCardPrice // 초기 hint로 되돌리기
                    dialogcardEditCardprice.backgroundTintList = emphasis_yellow
                }
                else if(!hasFocus && !dialogcardEditCardprice.text.isEmpty()){
                    dialogcardEditCardprice.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }

            dialogCardBtnPositive.setOnClickListener{
                if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "onResume: 카드 이름을 입력해주세요.", )
                    dismiss()
                    Toast.makeText(requireContext(), "카드 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else if(dialogcardEditCardname.text.toString() == ""){
                    Log.e("TAG", "onResume: 초기 금액을 입력해주세요")
                    dismiss()
                    Toast.makeText(requireContext(), "초기 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
                else{
                    var price = dialogcardEditCardprice.text.toString()

                    if(price.contains(",")){
                        price = price.replace(",", "")
                    }

                    activityViewModel.changeConnectedState(ConnectedState.CONNECTING)
                    activityViewModel.sendCardData(AppSendCardData(dialogcardEditCardname.text.toString(), price.toInt()))
                    newCard = 1
                    //TODO 이 코드가 과연 괜찮은 코드일까? 일단 해두고 다른 방법 찾아보기!
                    /// 카드를 추가했을 때 ShowPictureFragment에 변화된 것으로 알려주며 Observer할 때 myArray를 clear하고 새로 불러줘야됨!
                    val fragmentManager = requireActivity().supportFragmentManager
                    val showPictureFragment = fragmentManager.findFragmentByTag("showPictureFragment") as ShowPictureFragment?
                    showPictureFragment?.setNewCardValue(newCard)
                    dismiss()
                }
            }

            dialogCardBtnNegative.setOnClickListener{
                Log.e("TAG", "onResume: 카드 추가 취소", )
                dismiss()
            }
        }
    }
}