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
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordFrameBinding
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordFrameFragment : BaseFragment<FragmentRecordFrameBinding>(FragmentRecordFrameBinding::inflate, "RecordFrameFragment") {
    init { Log.e("TAG", "RecordFrameFragment", ) }

    private val viewModel: RecordViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var callback : OnBackPressedCallback
//    private val recordLocalFragment: RecordLocalFragment by lazy { RecordLocalFragment(viewModel) }
//    private val recordServerFragment: RecordServerFragment by lazy { RecordServerFragment(viewModel) }

    override fun initData() {}

    override fun initUI() {
        if(activityViewModel.selectedData.value?.type == ShowType.LOCAL)
            changeFragment(RecordLocalFragment(viewModel))
        else
            changeFragment(RecordServerFragment(viewModel))
        activityViewModel.removeSelectedData()
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
        //TODO data binding
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun changeFragment(fragment: Fragment){
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
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    companion object{
        private const val TAG = "RecyclerFragment"
    }
}