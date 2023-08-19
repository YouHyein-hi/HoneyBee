package com.example.receiptcareapp.ui.fragment.record

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.local.toRecyclerShowData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ShowType
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordLocalBinding
import com.example.receiptcareapp.dto.RecyclerData
import com.example.receiptcareapp.ui.adapter.RecordLocalAdapter
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-08-07
 * pureum
 */
@AndroidEntryPoint
class RecordLocalFragment(
    private val viewModel: RecordViewModel
) : BaseFragment<FragmentRecordLocalBinding>(
    FragmentRecordLocalBinding::inflate,
    "RecodeLocalFragment"
) {
    private val recordLocalAdapter: RecordLocalAdapter = RecordLocalAdapter()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun initData() {
        recordLocalAdapter.dataList.clear()
    }

    override fun initUI() {
        initLocalRecyclerView()
        viewModel.getLocalAllData()
//        activityViewModel.changeNullPicture()
    }

    override fun initListener() {
        //로컬 목록에서 리스트를 누를경우
        recordLocalAdapter.onLocalSaveClic = {
            Log.e("TAG", "initListener: local")
            //Activity ViewModel 값 저장
            activityViewModel.changeSelectedData(
                RecyclerData(
                    type = ShowType.LOCAL,
                    uid = it.uid,
                    cardName = it.cardName,
                    amount = it.amount,
                    billSubmitTime = it.date,
                    storeName = it.storeName,
                    file = it.file
                )
            )
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }
    }

    override fun initObserver() {
        //룸에서 받아온 데이터 옵져버
        viewModel.roomData.observe(viewLifecycleOwner) {
            recordLocalAdapter.dataList.clear()
            recordLocalAdapter.dataList = it.map { it.toRecyclerShowData() }.toMutableList()
            setTextAndVisible("데이터가 비었어요!", recordLocalAdapter.dataList.isEmpty())
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun initLocalRecyclerView() {
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = recordLocalAdapter
        recordLocalAdapter.dataList.clear()
    }

    private fun setTextAndVisible(text: String, state: Boolean) {
        binding.emptyText.text = text
        binding.emptyText.isVisible = state
    }

}