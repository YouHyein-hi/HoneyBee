package com.example.receiptcareapp.ui.fragment.record

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.local.toRecyclerShowData
import com.example.domain.model.ui.recycler.LocalRecyclerData
import com.example.receiptcareapp.R
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordLocalBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.domain.model.ui.recycler.ServerRecyclerData
import com.example.receiptcareapp.ui.adapter.RecordLocalAdapter
import com.example.receiptcareapp.util.FetchState
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
    private var dataList = mutableListOf<LocalRecyclerData>()

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
        recordLocalAdapter.onLocalSaveClick = {
            //Activity ViewModel 값 저장
            activityViewModel.changeSelectedData(
                RecyclerData(
                    type = ShowType.LOCAL,
                    uid = it.uid,
                    cardName = it.cardName,
                    amount = it.amount,
                    date = it.date,
                    storeName = it.storeName,
                    file = it.file
                )
            )
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }

        binding.recordLocalSearchTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recordLocalAdapter.dataList = dataList.filter { it.storeName.contains(s.toString()) }.toMutableList()

            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun initObserver() {
        //룸에서 받아온 데이터 옵져버
        //TODO 데이터바인딩 -> 데이터바인딩 옵저버할때 데이터가 비었는지 안비었는지 확인해주고 바꿔주기? (희망사항)
        viewModel.roomData.observe(viewLifecycleOwner) {
            recordLocalAdapter.dataList.clear()
            recordLocalAdapter.dataList = it.map { it -> it.toRecyclerShowData() }.toMutableList()
                .also { dataList = it }
            setTextAndVisible("데이터가 비었어요!", recordLocalAdapter.dataList.isEmpty())
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            when(it.second){
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> {emptyTextControl(true, "서버 연결 실패..")}
                FetchState.PARSE_ERROR -> {emptyTextControl(true, "서버 연결 실패..")}
            }
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun emptyTextControl(state: Boolean, massage: String = "데이터가 비었어요!"){
        binding.recordListEmptyTxt.isVisible = state
        binding.recordListEmptyTxt.text = massage
    }

    private fun initLocalRecyclerView() {
        binding.recordListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recordListRecyclerView.adapter = recordLocalAdapter
        recordLocalAdapter.dataList.clear()
    }

    private fun setTextAndVisible(text: String, state: Boolean) {
        binding.recordListEmptyTxt.text = text
        binding.recordListEmptyTxt.isVisible = state
    }

}