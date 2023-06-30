package com.example.receiptcareapp.ui.dialog

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.domain.model.UpdateData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.adapter.ShowPictureAdapter
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import java.time.LocalDateTime

class ChangeDialog : BaseDialog<DialogChangeBinding>(DialogChangeBinding::inflate) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var myData: RecyclerData
    private var myArray = arrayListOf<String>()
    private var checked = ""
    private var cardId = 0
    private var settingYear = 0
    private var settingMonth = 0
    private var settingDay = 0
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0
    private var newDate = listOf<String>()

    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            myData = activityViewModel.selectedData.value!!
            newDate = myData.billSubmitTime.replace(" ", "").split("년", "월", "일", "시", "분", "초")
            Log.e("TAG", "initData myData : $myData", )
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()

        }
    }

    override fun initUI() {
        getSpinner()

        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 1), ViewGroup.LayoutParams.WRAP_CONTENT)
        val dataCardName = myData.cardName

        activityViewModel.cardData.observe(viewLifecycleOwner) {
            myArray.clear()
            it.forEach { myArray.add("${it.cardName}  :  ${it.cardAmount}") }
            val adapter = ShowPictureAdapter(requireContext(), myArray)
            binding.changeCardspinner.adapter = adapter

            var position = -1
            for (i in 0 until adapter.count) {
                val item = adapter.getItem(i)
                if (item!!.startsWith("$dataCardName ")) {
                    position = i
                    break
                }
            }
            if (position != -1) {
                binding.changeCardspinner.setSelection(position)
            }
            else{
                dismiss()
                showShortToast("카드 불러오기 실패!")
            }
        }
        Log.e("TAG", "onCreateView: ${myData.cardName}", )
        Log.e("TAG", "onCreateView: ${myData}", )
        val newDate = if(myData.billSubmitTime.contains("년"))
                        myData.billSubmitTime.replace(" ", "").split("년","월","일","시","분","초")
                    else
                        myData.billSubmitTime.split("-","T",":")

        Log.e("TAG", "onCreateView: ${newDate}", )

        // 수정 전 로컬 데이터 화면에 띄우기
        // Spinner은 아직 설정 안함
        binding.changeBtnStore.setText(myData.storeName)
        binding.changeBtnPrice.setText(myData.amount)
        settingYear = newDate[0].toInt()
        settingMonth = newDate[1].toInt()
        settingDay = newDate[2].toInt()
        binding.changeDatepicker.init(settingYear, settingMonth - 1, settingDay, null)
    }

    override fun initListener() {
        binding.changeBtnPositive.setOnClickListener {
            myYear = binding.changeDatepicker.year
            myMonth = binding.changeDatepicker.month + 1
            myDay = binding.changeDatepicker.dayOfMonth
            Log.e("TAG", "onCreateDialog: $myYear, $myMonth, $myDay")

            val myLocalDateTime = LocalDateTime.of(
                myYear, myMonth, myDay,
                LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
            )
            val data = activityViewModel.selectedData.value

            when {
                checked == "" -> { showShortToast("카드를 입력하세요.") }
                binding.changeBtnStore.text!!.isEmpty() -> { showShortToast("가게 이름을 입력하세요.") }
                binding.changeBtnPrice.text!!.isEmpty() -> { showShortToast("금액을 입력하세요.") }
                myLocalDateTime.toString() == "" -> { showShortToast("날짜를 입력하세요.") }
                data?.file == null -> { showShortToast("사진이 비었습니다.") }
                else -> {
                    Log.e("TAG", "initListener myData: $myData", )
                    if(myData.type == ShowType.SERVER) {
                        activityViewModel.updateServerData(
                            sendData = UpdateData(
                                billSubmitTime = myLocalDateTime.toString(),
                                amount = binding.changeBtnPrice.text.toString(),
                                cardName = checked,
                                storeName = binding.changeBtnStore.text.toString()
                            ),
                            uid = myData.uid,
                        )
                    }else{
                        activityViewModel.resendData(
                            sendData = AppSendData(
                                billSubmitTime = myLocalDateTime.toString(),
                                amount = binding.changeBtnPrice.text.toString(),
                                cardName = checked,
                                storeName = binding.changeBtnStore.text.toString(),
//                                picture = activityViewModel.bitmapToUri(requireActivity(),activityViewModel.picture.value)
                                picture = myData.file!!
                            ),
                            myData.uid
                        )
                    }
                    dismiss()
                }
            }
        }

        binding.changeBtnNegative.setOnClickListener {
            dismiss()
        }
    }

    override fun initObserver() {
        activityViewModel.cardData.observe(viewLifecycleOwner) {
            myArray.clear()
            it.forEach { myArray.add("${it.cardName}  :  ${it.cardAmount}") }
            val adapter = ShowPictureAdapter(requireContext(), myArray)
            binding.changeCardspinner.adapter = adapter
        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner) {
            Log.e("TAG", "onViewCreated: $it")
        }
    }

    private fun getSpinner() {
        activityViewModel.receiveServerCardData()
        val adapter = ShowPictureAdapter(requireContext(), myArray)

        binding.changeCardspinner?.adapter = adapter
        binding.changeCardspinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 여기서 position은 0부터 시작함
                    val spiltCard = myArray[position].split(" : ")
                    cardId = position
                    checked = spiltCard[0].replace(" ","")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }
}
