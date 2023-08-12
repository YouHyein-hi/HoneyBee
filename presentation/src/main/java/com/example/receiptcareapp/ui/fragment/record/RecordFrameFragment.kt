package com.example.receiptcareapp.ui.fragment.record

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.ui.adapter.RecordLocalAdapter
import com.example.receiptcareapp.ui.adapter.RecordServerAdapter
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordFrameBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.viewModel.fragmentViewModel.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordFrameFragment : BaseFragment<FragmentRecordFrameBinding>(FragmentRecordFrameBinding::inflate, "RecordFrame") {
    init { Log.e("TAG", ": RecyclerFragment RecyclerFragment RecyclerFragment", ) }

    private val viewModel: RecordViewModel by viewModels()
    private lateinit var callback : OnBackPressedCallback

    override fun initData() {}

    override fun initUI() {
        Log.e(TAG, "initUI", )
        changeFragment(RecordServerFragment(viewModel))
    }

    override fun initListener() {
        // 하단 바텀시트 버튼
        binding.bottomNavigationView.setOnItemSelectedListener{
            parentFragmentManager.beginTransaction()
            when(it.itemId){
                R.id.server -> {
                    changeFragment(RecordServerFragment(viewModel))
                    true
                }
                R.id.local -> {
                    changeFragment(RecordLocalFragment(viewModel))
                    true
                }
                else -> {true}
            }
        }
        //뒤로가기 버튼
        binding.baseComponent.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun initObserver() {
        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }
    }

    fun changeFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id,fragment).commit()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_recyclerFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    companion object{
        private const val TAG = "RecyclerFragment"
    }
}