package com.example.receiptcareapp.ui.fragment

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.ui.botteomSheet.CardBottomSheet
import com.example.receiptcareapp.ui.dialog.AddDialog

//메인 프레그먼트/
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, "HomeFragment") {

    private lateinit var callback: OnBackPressedCallback
    private val homeCardBottomSheet: CardBottomSheet = CardBottomSheet()
    private val addDialog : AddDialog = AddDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
                    .setTitle("종료")
                    .setMessage("꿀을 그만 빠시겠어요?")
                    .setPositiveButton("그만 빤다"){dialog, id->
                        requireActivity().finish()
                    }
                    .setNegativeButton("더 빤다"){dialog, id->

                    }.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun initData() {}

    override fun initUI() {
        with(binding){
            historyBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment)}
            settingBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }

            addBtn.setOnClickListener{ addDialog.show(parentFragmentManager, "addDialog") }
            cardlistBtn.setOnClickListener{ homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet") }
            //addBtn
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {}

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}