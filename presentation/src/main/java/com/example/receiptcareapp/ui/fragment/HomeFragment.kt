package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.ui.adapter.HomeCardAdapter
import com.example.receiptcareapp.ui.botteomSheet.CardBottomSheet
import com.example.receiptcareapp.ui.dialog.AddDialog
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, "HomeFragment") {

    private val viewModel : HomeCardViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback
    private lateinit var homeCardBottomSheet: CardBottomSheet
    private val addDialog : AddDialog = AddDialog()
    private val adapter: HomeCardAdapter = HomeCardAdapter()

    override fun initData() {
        homeCardBottomSheet = CardBottomSheet(viewModel)
    }

    override fun initUI() {
        with(binding){
            historyBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment) }
            settingBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }
            noticeBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_noticeFragment) }

            addBtn.setOnClickListener{ addDialog.show(parentFragmentManager, "addDialog") }
            cardListBtn.setOnClickListener{
                homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet")
            }
            homeCardRecyclerview.setOnClickListener{ homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet") }
            homeCardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            homeCardRecyclerview.adapter = adapter
        }
        //카드목록, 공지사항 불러오기
        viewModel.getServerCardData()
        viewModel.getNoticeList()
    }

    override fun initListener() {
    }

    override fun initObserver() {
        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.cardList.observe(viewLifecycleOwner) { dataList ->
            if (dataList.isEmpty()) {/*setCenterText("데이터가 비었어요!", true)*/ }
            else { adapter.dataList = dataList }
        }

        viewModel.notice.observe(viewLifecycleOwner){
            binding.homeNoticeTxt.text = it
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

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
}