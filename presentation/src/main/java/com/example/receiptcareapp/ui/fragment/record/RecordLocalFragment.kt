package com.example.receiptcareapp.ui.fragment.record

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.example.receiptcareapp.ui.adapter.RecordLocalAdapter
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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_array,
            R.layout.spinner_custom_record_layout
        ).also {
            binding.recordFilterSpinner.adapter = it
        }
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

        binding.recordFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    //기본순
                    0 -> recordLocalAdapter.dataList = dataList.toMutableList()
                    //최신순
                    1 -> recordLocalAdapter.dataList = dataList.sortedBy { it.date }.toMutableList()
                    //오래된순
                    2 -> recordLocalAdapter.dataList = dataList.sortedBy { it.date }.reversed().toMutableList()
                    //카드순
                    3 -> recordLocalAdapter.dataList = dataList.sortedBy { it.cardName }.toMutableList()
                    //높은 금액순
                    4 -> recordLocalAdapter.dataList = dataList.sortedBy { it.amount }.toMutableList()
                    //낮은 금액순
                    5 -> recordLocalAdapter.dataList = dataList.sortedBy { it.amount }.reversed().toMutableList()
                    //등록 이름순
//                    6 -> recordServerAdapter.dataList = dataList.sortedBy { it.storeAmount }.toMutableList()
                }
                Log.e("TAG", "onItemSelected position: $position", )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

/*        binding.recordRefresh.setOnRefreshListener {
            binding.recordRefresh.isRefreshing = false
            viewModel.getLocalAllData()
        }*/
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