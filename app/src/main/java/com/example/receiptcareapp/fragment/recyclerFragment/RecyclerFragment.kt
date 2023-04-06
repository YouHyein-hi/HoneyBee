package com.example.receiptcareapp.fragment.recyclerFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.toDomainRecyclerData
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
    init {
        Log.e("TAG", ": recyclerfragment Start", )
    }

    private val activityViewModel: MainViewModel by activityViewModels()
    private val fragmentViewModel : FragmentViewModel by viewModels({requireActivity()})
    private val serverAdapter: ServerAdapter = ServerAdapter()
    private val localAdapter: LocalAdapter = LocalAdapter()
    private lateinit var callback : OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate: RecyclerFragment start", )
        fragmentViewModel.changeStartGap("server")
//        activityViewModel.insertData("하이하이", "10,000", "아우네순댓국", "2023-03-20T20:20:20", "content://media/external/images/media/1000014186")
//        activityViewModel.insertData("하이하이", "10,000", "아우네순댓국", "2023-03-20T20:20:21", "content://media/external/images/media/1000014186")
//        activityViewModel.insertData("하이하이", "10,000", "아우네순댓국", "2023-03-20T20:20:22", "content://media/external/images/media/1000014186")
//        activityViewModel.insertData("하이하이", "10,000", "아우네순댓국", "2023-03-20T20:20:23", "content://media/external/images/media/1000014186")
//        activityViewModel.insertData("하이하이", "10,000", "아우네순댓국", "2023-03-20T20:20:24", "content://media/external/images/media/1000014186")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 통신연결, 서버상태 값 초기화
        activityViewModel.changeConnectedState(ConnetedState.DISCONNECTED)
        activityViewModel.changeServerState(ServerState.NONE)

        // recycler show에서 넘어왔을땐

        //넘겨받는 데이터의 값을 초기화시키기.
        fragmentViewModel.myShowLocalData(null)
        fragmentViewModel.myShowServerData(null)

        //서버에서 받아온 데이터
        activityViewModel.serverData.observe(viewLifecycleOwner){
            serverAdapter.dataList = it
            setTextAndVisible("데이터가 비었어요!", serverAdapter.dataList.isEmpty())
        }

        //룸에서 받아온 데이터
        activityViewModel.roomData.observe(viewLifecycleOwner){
            localAdapter.dataList = it
            setTextAndVisible("데이터가 비었어요!", localAdapter.dataList.isEmpty())
        }



        if(fragmentViewModel.startGap.value == "server"){
            Log.e("TAG", "서버부분!", )
            activityViewModel.getAllServerData()
            initServerRecyclerView()
            binding.explain.text = "서버의 데이터 입니다."
            //리사이클러뷰 초기화
        }
        else if(fragmentViewModel.startGap.value == "local"){
            Log.e("TAG", "로컬부분!", )
            binding.bottomNavigationView.menu.findItem(R.id.local).isChecked = true
            initLocalRecyclerView()
            activityViewModel.getAllRoomData()
            binding.explain.text = "휴대폰의 데이터 입니다."
        }



        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: $it", )
            if(it==ConnetedState.DISCONNECTED) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            else binding.progressBar.visibility = View.VISIBLE
        }

        //서버 연결 안될 시 배경에 보여주기
        activityViewModel.serverState.observe(viewLifecycleOwner){
            Log.e("TAG", "onViewCreated: inin! $it", )
            if(it==ServerState.FALSE) {
                setTextAndVisible("서버 연결 실패!", true)
            }else{
                setTextAndVisible("", false)
            }
        }

        // 하단 바텀시트 버튼
        binding.bottomNavigationView.setOnItemSelectedListener{
            setTextAndVisible("",false)
            when(it.itemId){
                R.id.server -> {
//                    activityViewModel.hideServerCoroutineStop()
                    binding.explain.text = "서버의 데이터 입니다."
                    activityViewModel.getAllServerData()
                    initServerRecyclerView()
                    true
                }
                R.id.local -> {
                    Log.e("TAG", "click listener local: ", )
                    activityViewModel.hideServerCoroutineStop()
                    setTextAndVisible("",false)
                    binding.explain.text = "휴대폰의 데이터 입니다."
                    activityViewModel.getAllRoomData()
                    initLocalRecyclerView()
                    true
                }
                else -> {true}
            }
        }

        serverAdapter.onServerSaveClick = {
            fragmentViewModel.myShowServerData(it)
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
            fragmentViewModel.changeStartGap("server")
        }


        localAdapter.onLocalSaveClic = {
            Log.e("TAG", "onViewCreated: $it", )
            fragmentViewModel.myShowLocalData(it.toDomainRecyclerData())
            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
            fragmentViewModel.changeStartGap("local")
        }


        //뒤로가기 버튼
        binding.imageBack.setOnClickListener{
            findNavController().popBackStack()
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

    fun setTextAndVisible(text:String, state:Boolean){
        binding.backgroundText.text = text
        binding.backgroundText.isVisible = state
    }




    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activityViewModel.connectedState.value == ConnetedState.CONNECTING) {
                    activityViewModel.serverCoroutineStop()
                    findNavController().navigate(R.id.action_recyclerFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_recyclerFragment_to_homeFragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}