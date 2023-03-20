package com.example.receiptcareapp.fragment.recyclerFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnetedState
import com.example.receiptcareapp.State.ServerState
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import com.example.receiptcareapp.fragment.recyclerFragment.adapter.LocalAdapter
import com.example.receiptcareapp.fragment.recyclerFragment.adapter.ServerAdapter
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel


class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    private val activityViewModel: MainViewModel by activityViewModels()
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val serverAdapter: ServerAdapter = ServerAdapter()
    private val localAdapter: LocalAdapter = LocalAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 통신연결, 서버상태 값 초기화
        activityViewModel.changeConnectedState(ConnetedState.DISCONNECTED)
        activityViewModel.changeServerState(ServerState.NONE)

        //넘겨받는 데이터의 값을 초기화시키기.
        fragmentViewModel.myShowLocalData(null)
        fragmentViewModel.myShowServerData(null)

        initServerRecyclerView()

        activityViewModel.getAllServerData()

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            if(it==ConnetedState.DISCONNECTED) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            else binding.progressBar.visibility = View.VISIBLE
        }


        //서버에서 받아온 데이터
        activityViewModel.serverData.observe(viewLifecycleOwner){
            serverAdapter.dataList = it
            setBackgroundText("데이터가 비었어요!", serverAdapter.dataList.isNotEmpty())
        }

        //룸에서 받아온 데이터
        activityViewModel.roomData.observe(viewLifecycleOwner){
            localAdapter.dataList = it
            setBackgroundText("데이터가 비었어요!", serverAdapter.dataList.isNotEmpty())
        }

        //서버 연결 안될 시 배경에 보여주기
        activityViewModel.serverState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: inin!", )
            if(it==ServerState.FALSE) {
                setBackgroundText("서버 연결 실패!", true)
            }else{
                setBackgroundText("", false)
            }
        }

        // 하단 바텀시트 버튼
        binding.bottomNavigationView.setOnItemSelectedListener{
            setBackgroundText("",false)
            when(it.itemId){
                R.id.server -> {
                    binding.explain.text = "서버의 데이터 입니다."
                    activityViewModel.getAllServerData()
                    initServerRecyclerView()
                    true
                }
                R.id.local -> {
                    binding.explain.text = "휴대폰의 데이터 입니다."
                    activityViewModel.getAllLocalData()
                    initLocalRecyclerView()
                    true
                }
                else -> {true}
            }
        }

        serverAdapter.onServerSaveClick = {
            fragmentViewModel.myShowServerData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }

        localAdapter.onLocalSaveClic = {
            fragmentViewModel.myShowLocalData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }

        binding.imageBack.setOnClickListener{
            findNavController().navigate(R.id.action_recyclerFragment_to_homeFragment)
        }
    }

    fun initServerRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = serverAdapter
    }
    fun initLocalRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = localAdapter
    }

    fun setBackgroundText(text:String, state:Boolean){
        binding.backgroundText.text = text
        binding.backgroundText.isVisible = state
    }
}