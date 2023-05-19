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
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.receiptcareapp.dto.ShowData
import com.example.receiptcareapp.fragment.showPictureFragment.SpinnerCustomAdapter
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import java.util.*

class ChangeDialog : DialogFragment() {

    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var binding : DialogChangeBinding
    private lateinit var myData: RecyclerShowData
    private var myArray = arrayListOf<String>()
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

        //val spinner = view.findViewById<Spinner>(R.id.change_spinner)
        //val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArray)
            //SpinnerCustomAdapter(requireContext(), myArray)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinner.adapter = adapter

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


        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChangeBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

//        myData = ShowData(ShowType.LOCAL, data!!.uid, data.cardName, data.amount, data.date, data.storeName, data.file)
        myData = fragmentViewModel.showLocalData.value!!
        val newDate = myData.date.split("년","월","일","시","분","초")

        val changeDialog = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
            .setTitle("")
            .setMessage("데이터를 수정하시겠어요?")
            .setView(binding.root)
            .setPositiveButton("수정해서 보내기") { _, _ ->
//                activityViewModel.changeServerData(SendData(
//                    id = myData.id, cardName = myData.cardName, amount = myData.date, date = myData.date, picture = myData.picture, storeName = myData.pictureName))
                //                    findNavController().popBackStack()
                myYear = binding.changeDatepicker.year
                myMonth = binding.changeDatepicker.month + 1
                myDay = binding.changeDatepicker.dayOfMonth
                Log.e("TAG", "onCreateDialog: $myYear, $myMonth, $myDay")
            }
            .setNegativeButton("닫기") { _,  _ ->
                // Cancel 버튼을 클릭했을 때 처리하는 코드를 작성합니다.
            }
            .create()

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

        getSpinner()




        //myArray.add("하아히어ㅣㅇ")
        /*
        activityViewModel.receiveServerCardData()
        val adapter = SpinnerCustomAdapter(requireContext(), myArray)
            // SpinnerCustomAdapter(requireContext(), myArray)
            // ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArray)

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.changeCardspinner?.adapter = adapter
        binding.changeCardspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.e("TAG", "onItemSelected: ${myArray[position]}", )
                Log.e("TAG", "onItemSelected: ${myArray[position]}", )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        */

        changeDialog.setOnShowListener{
            positiveButton = changeDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            negativeButton = changeDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

            positiveButton?.setTextColor(Color.RED)
            negativeButton?.setTextColor(Color.BLACK)
        }
        // 다이얼로그를 생성하고 반환합니다.
        return changeDialog
    }

    private fun getSpinner(){
        Log.e("TAG", "getSpinner", )
        activityViewModel.receiveServerCardData()
        var adapter = SpinnerCustomAdapter(requireContext(), myArray)

        binding.changeCardspinner.adapter = adapter
        binding.changeCardspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                Log.e("TAG", "onItemSelected: ${myArray[position]}", )
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}
