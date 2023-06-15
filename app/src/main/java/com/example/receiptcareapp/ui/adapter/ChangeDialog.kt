package com.example.receiptcareapp.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.domain.model.RecyclerShowData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.time.LocalDateTime

class ChangeDialog : DialogFragment() {

    //viewModels 뷰모델의 객체를 생성주는것?
    private val fragmentViewModel : FragmentViewModel by activityViewModels()

    //엑티비티를 따라가는 뷰모델이니까
    // 엑티비티의 생명주기 따라가는 뷰모델이 뭘까?
    private val activityViewModel: MainViewModel by activityViewModels()

    private lateinit var binding : DialogChangeBinding
    private lateinit var myData: RecyclerShowData
    private var myArray = arrayListOf<String>()
    private var checked = ""
    private var cardId = 0
    private var settingYear = 0
    private var settingMonth = 0
    private var settingDay = 0
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val cardData = activityViewModel.cardData.value

        if (cardData != null) {
            myArray.clear()
            cardData.forEach { myArray.add("${it.cardName}  :  ${it.cardAmount}") }
            val adapter = ShowPictureAdapter(requireContext(), myArray)
            binding.changeCardspinner.adapter = adapter
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogChangeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.cardData.observe(viewLifecycleOwner) {
            myArray.clear()
            it.forEach { myArray.add("${it.cardName}  :  ${it.cardAmount}") }
            val adapter = ShowPictureAdapter(requireContext(), myArray)
            binding.changeCardspinner.adapter = adapter
            //binding.spinner.adapter = adapter
            //adapter.notifyDataSetChanged()
        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
        }


        myData = fragmentViewModel.showLocalData.value!!
        val newDate = myData.date.split("년","월","일","시","분","초")

        Log.e("TAG", "onCreateView: ${myData.cardName}", )

        // 수정 전 로컬 데이터 화면에 띄우기
        // Spinner은 아직 설정 안함
        binding.changeCardspinner
        binding.changeBtnStore.setText(myData.storeName)
        binding.changeBtnPrice.setText(myData.amount)
        try {
            settingYear = newDate[0].toInt()
            settingMonth = newDate[1].toInt()
            settingDay = newDate[2].toInt()
        }catch (E:Exception){
            settingYear = 2023
            settingMonth = 5
            settingDay = 16
        }
        binding.changeDatepicker.init(settingYear, settingMonth-1, settingDay, null)
        Log.e("TAG", "onCreateView: ${settingYear}, ${settingMonth}, ${settingYear}", )


        binding.changeBtnPositive.setOnClickListener{
//                activityViewModel.changeServerData(SendData(
//                    id = myData.id, cardName = myData.cardName, amount = myData.date, date = myData.date, picture = myData.picture, storeName = myData.pictureName))
            //                    findNavController().popBackStack()
            myYear = binding.changeDatepicker.year
            myMonth = binding.changeDatepicker.month + 1
            myDay = binding.changeDatepicker.dayOfMonth
            Log.e("TAG", "onCreateDialog: $myYear, $myMonth, $myDay")

            val myLocalDateTime = LocalDateTime.of(
                myYear, myMonth, myDay,
                LocalDateTime.now().hour, LocalDateTime.now().minute, LocalDateTime.now().second
            )

            Log.e("TAG", "onCreateView: ${myData.uid}", )
            Log.e("TAG", "onCreateDialog: ${myLocalDateTime}, ${binding.changeBtnPrice.text}, ${checked}, ${binding.changeBtnStore.text}, ${myData.file}", )

            val data = fragmentViewModel.showLocalData.value

            if(checked == " "){
                Log.e("TAG", "onCreateView: 카드가 비어있습니다.", )
            }
            else if(binding.changeBtnStore.text!!.isEmpty()){
                Log.e("TAG", "onCreateView: 가게 이름이 비어있습니다.", )
            }
            else if(binding.changeBtnPrice.text!!.isEmpty()){
                Log.e("TAG", "onCreateView: 금액이 비어있습니다.", )
            }
            else if(myLocalDateTime.toString() == ""){
                Log.e("TAG", "onCreateView: 날짜가 비어있습니다.", )
            }
            else if(data?.file == null){
                Log.e("TAG", "onCreateView: 이미지가 비어있습니다.", )
            }
            else{
                Log.e("TAG", "onCreateView: 다 있음!", )
                activityViewModel.changeServerData(
                    AppSendData(
                        date = myLocalDateTime.toString(), amount = binding.changeBtnPrice.text.toString(), cardName = checked, picture = data!!.file, storeName = binding.changeBtnStore.text.toString())
                    , myData.uid
                )
            }

            dismiss()
        }

        binding.changeBtnNegative.setOnClickListener{
            dismiss()
        }

        getSpinner()
    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 1).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun getSpinner(){
        Log.e("TAG", "getSpinner", )

        activityViewModel.receiveServerCardData()
        val adapter = ShowPictureAdapter(requireContext(), myArray)

        binding.changeCardspinner?.adapter = adapter
        binding.changeCardspinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 여기서 position은 0부터 시작함
                Log.e("TAG", "getSpinner onItemSelected: ${position}", )
                Log.e("TAG", "getSpinner onItemSelected: ${myArray[position]}", )
                val spiltCard = myArray[position].split(" : ")
                cardId = position
                checked = spiltCard[0]
                Log.e("TAG", "onItemSelected: ${checked}", )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }
}
