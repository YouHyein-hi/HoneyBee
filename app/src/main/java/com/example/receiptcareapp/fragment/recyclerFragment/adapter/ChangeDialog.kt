package com.example.receiptcareapp.fragment.recyclerFragment.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.domain.model.RecyclerShowData
import com.example.domain.model.send.AppSendData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.dto.ShowData
import com.example.receiptcareapp.fragment.showPictureFragment.SpinnerCustomAdapter
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.time.LocalDateTime
import java.util.*

class ChangeDialog : DialogFragment() {

    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var binding : DialogChangeBinding
    private lateinit var myData: RecyclerShowData
    private var myArray = arrayListOf<String>()
    private var checked = ""
    private var settingYear = 0
    private var settingMonth = 0
    private var settingDay = 0
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0
    private var positiveButton: Button? = null
    private var negativeButton: Button? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogChangeBinding.inflate(layoutInflater)

        activityViewModel.cardData.observe(viewLifecycleOwner) {
            myArray.clear()
            it.forEach { myArray.add("${it.cardName}  :  ${it.cardAmount}") }
            val adapter = SpinnerCustomAdapter(requireContext(), myArray)
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

        // 수정 전 로컬 데이터 화면에 띄우기
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
            )  // 값이 오늘로 설정되어있음. datePicker에서 설정한 날짜로 보내지게 하기
            val changePrice = binding.changeBtnPrice.text.toString()
//                activityViewModel.changeServerData(
//                    AppSendData(
//                        date = myLocalDateTime.toString(), amount = binding.changeBtnPrice.text.toString(), cardName = checked, picture = fragmentViewModel.image.value!!, storeName = binding.changeBtnStore.text.toString())
//                )
            Log.e("TAG", "onCreateDialog: ${myLocalDateTime}, ${changePrice}, ${/*checked*/myData.cardName}, ${binding.changeBtnStore.text}, ${myData.file}", )

            dismiss()
        }

        binding.changeBtnNegative.setOnClickListener{
            dismiss()
        }

        getSpinner()

        return binding.root
    }

    private fun getSpinner(){
        Log.e("TAG", "getSpinner", )

        activityViewModel.receiveServerCardData()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArray)
        // SpinnerCustomAdapter(requireContext(), myArray)
        // ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArray)

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.changeCardspinner?.adapter = adapter
        binding.changeCardspinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("TAG", "getSpinner onItemSelected: ${position}", )
                Log.e("TAG", "getSpinner onItemSelected: ${myArray[position]}", )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
