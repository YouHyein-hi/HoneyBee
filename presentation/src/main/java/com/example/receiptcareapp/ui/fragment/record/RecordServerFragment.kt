package com.example.receiptcareapp.ui.fragment.record

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.remote.receive.bill.toServerRecyclerData
import com.example.receiptcareapp.R
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentRecordServerBinding
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.domain.model.ui.recycler.ServerRecyclerData
import com.example.receiptcareapp.ui.adapter.RecordServerAdapter
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 2023-08-07
 * pureum
 */

@AndroidEntryPoint
class RecordServerFragment(
    private val viewModel: RecordViewModel
) : BaseFragment<FragmentRecordServerBinding>(
    FragmentRecordServerBinding::inflate,
    "RecyclerFragment"
) {
    private val recordServerAdapter: RecordServerAdapter = RecordServerAdapter()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var dataList = mutableListOf<ServerRecyclerData>()

    override fun initData() {}

    override fun initUI() {
        initServerRecyclerView()
        viewModel.getServerAllBillData()
        recordServerAdapter.dataList.clear()
        binding.recordFilterSpinner.adapter

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            R.layout.spinner_custom_record_layout
        ).also {
            binding.recordFilterSpinner.adapter = it
        }
    }

    override fun initListener() {
        //서버 목록에서 리스트를 누를 경우
        recordServerAdapter.onServerSaveClick = {
            activityViewModel.changeSelectedData(
                RecyclerData(
                    type = ShowType.SERVER,
                    uid = it.uid!!,
                    cardName = it.cardName,
                    amount = it.storeAmount,
                    date = it.date,
                    storeName = it.storeName,
                    file = null,
                )
            )
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }

        binding.recordServerSearchTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recordServerAdapter.dataList = dataList.filter { it.storeName.contains(s.toString()) }.toMutableList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.recordFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    //기본순
                    0 -> recordServerAdapter.dataList = dataList.toMutableList()
                    //최신순
                    1 -> recordServerAdapter.dataList = dataList.sortedBy { it.date }.toMutableList()
                    //오래된순
                    2 -> recordServerAdapter.dataList = dataList.sortedBy { it.date }.reversed().toMutableList()
                    //카드순
                    3 -> recordServerAdapter.dataList = dataList.sortedBy { it.cardName }.toMutableList()
                    //높은 금액순
                    4 -> recordServerAdapter.dataList = dataList.sortedBy { it.storeAmount }.toMutableList()
                    //낮은 금액순
                    5 -> recordServerAdapter.dataList = dataList.sortedBy { it.storeAmount }.reversed().toMutableList()
                    //등록 이름순
//                    6 -> recordServerAdapter.dataList = dataList.sortedBy { it.storeAmount }.toMutableList()
                }
                Log.e("TAG", "onItemSelected position: $position", )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.recordListRecyclerViewRefresh.setOnRefreshListener {
            binding.recordListRecyclerViewRefresh.isRefreshing = false
            viewModel.getServerAllBillData()
        }
    }

    override fun initObserver() {
        //서버에서 받아온 데이터 옵져버
        //TODO 데이터바인딩
        viewModel.billList.observe(viewLifecycleOwner) { it->
            recordServerAdapter.dataList.clear()
            recordServerAdapter.dataList = it?.body?.map { body-> body.toServerRecyclerData() }!!.toMutableList()
                .also { dataList = it }
            emptyTextControl(recordServerAdapter.dataList.isEmpty(),"데이터가 비었어요!", )
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            when(it.second){
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> {emptyTextControl(true, "서버 연결 실패..")}
                FetchState.PARSE_ERROR -> {emptyTextControl(true, "서버 오류..")}
            }
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun emptyTextControl(state: Boolean, massage: String = "데이터가 비었어요!"){
        binding.recordListEmptyTxt.isVisible = state
        binding.recordListEmptyTxt.text = massage
    }

    private fun initServerRecyclerView() {
        binding.recordListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recordListRecyclerView.adapter = recordServerAdapter
        recordServerAdapter.dataList.clear()
    }
}