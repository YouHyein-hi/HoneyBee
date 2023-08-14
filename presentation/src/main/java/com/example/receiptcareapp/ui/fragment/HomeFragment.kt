package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.ui.adapter.CardAdapter
import com.example.receiptcareapp.ui.adapter.CardHomeAdapter
import com.example.receiptcareapp.ui.botteomSheet.CardBottomSheet
import com.example.receiptcareapp.ui.dialog.AddDialog
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, "HomeFragment") {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel : HomeCardViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback
    private val homeCardBottomSheet: CardBottomSheet = CardBottomSheet()
    private val addDialog : AddDialog = AddDialog()
    private val adapter: CardHomeAdapter = CardHomeAdapter()
    private var uid : Long = 0

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

    override fun initData() {
    }

    override fun initUI() {
        with(binding){
            historyBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment) }
            menuBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }
            noticeBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_noticeFragment) }

            addBtn.setOnClickListener{ addDialog.show(parentFragmentManager, "addDialog") }
            cardlistBtn.setOnClickListener{ homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet") }
            cardhomeRecyclerview.setOnClickListener{
                Log.e("TAG", "initUI: linearLayout2 클릭함", )
                homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet") }

            cardhomeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            cardhomeRecyclerview.adapter = adapter
            //addBtn
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
        viewModel.getServerCardData()

        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        activityViewModel.cardData.observe(viewLifecycleOwner) { dataList ->
            if (dataList.isEmpty()) { //setCenterText("데이터가 비었어요!", true)
            } else {
                Log.e("TAG", "onCreateView: dataList  ${dataList}", )
                for (data in dataList) {
                    Log.e("TAG", "onCreateView: data  ${data}", )
                    uid = data.uid
                    // uid 값을 사용하여 원하는 작업 수행
                    Log.e("TAG", "uid: $uid")
                }
                //setCenterText("", false)
                adapter.dataList = dataList
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}