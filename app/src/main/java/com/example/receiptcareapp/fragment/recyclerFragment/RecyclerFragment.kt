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
import com.example.domain.model.local.toRecyclerShowData
import com.example.domain.model.receive.DomainReceiveAllData
import com.example.receiptcareapp.R
import com.example.receiptcareapp.State.ConnectedState
import com.example.receiptcareapp.databinding.FragmentRecyclerBinding
import com.example.receiptcareapp.fragment.recyclerFragment.adapter.LocalAdapter
import com.example.receiptcareapp.fragment.recyclerFragment.adapter.ServerAdapter
import com.example.receiptcareapp.fragment.viewModel.FragmentViewModel
import com.example.receiptcareapp.viewModel.MainViewModel
import com.example.receiptcareapp.base.BaseFragment


class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {
    init {
        Log.e("TAG", ": RecyclerFragment RecyclerFragment RecyclerFragment", )
    }

    private val activityViewModel: MainViewModel by activityViewModels()
    private val fragmentViewModel : FragmentViewModel by activityViewModels()
    private val serverAdapter: ServerAdapter = ServerAdapter()
    private val localAdapter: LocalAdapter = LocalAdapter()
    private lateinit var callback : OnBackPressedCallback


    override fun initData() {
        fragmentViewModel.changeStartGap("server")
        //init
        activityViewModel.nullPicture()
        // 통신연결, 서버상태 값 초기화
        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
        //넘겨받는 데이터의 값을 초기화시키기.
        fragmentViewModel.myShowLocalData(null)
        fragmentViewModel.myShowServerData(null)
    }

    override fun initUI() {
        if(fragmentViewModel.startGap.value == "server"){
            Log.e("TAG", "서버부분!", )
            activityViewModel.receiveServerAllData()
            initServerRecyclerView()
            binding.explain.text = "서버의 데이터 입니다."
        }
        else if(fragmentViewModel.startGap.value == "local"){
            Log.e("TAG", "로컬부분!", )
            binding.bottomNavigationView.menu.findItem(R.id.local).isChecked = true
            initLocalRecyclerView()
            activityViewModel.receiveAllRoomData()
            binding.explain.text = "휴대폰의 데이터 입니다."
        }
    }

    override fun initListener() {
        // 하단 바텀시트 버튼
        binding.bottomNavigationView.setOnItemSelectedListener{
            setTextAndVisible("",false)
            when(it.itemId){
                R.id.server -> {
                    binding.explain.text = "서버의 데이터 입니다."
                    activityViewModel.receiveServerAllData()
                    initServerRecyclerView()
                    true
                }
                R.id.local -> {
                    Log.e("TAG", "click listener local: ", )
                    binding.explain.text = "휴대폰의 데이터 입니다."
                    activityViewModel.hideServerCoroutineStop()
                    setTextAndVisible("",false)
                    activityViewModel.receiveAllRoomData()
                    initLocalRecyclerView()
                    true
                }
                else -> {true}
            }
        }

        //서버 목록에서 리스트를 누를 경우
        serverAdapter.onServerSaveClick = {
            Log.e("TAG", "onViewCreated: 눌렀다", )
            Log.e("TAG", "onViewCreated onViewCreated: $it", )
            activityViewModel.nullPicture()
            activityViewModel.receiveServerPictureData(it.uid)
            fragmentViewModel.myShowServerData(it)
            fragmentViewModel.changeStartGap("server")
        }

        //로컬 목록에서 리스트를 누를경우
        localAdapter.onLocalSaveClic = {
            Log.e("TAG", "onViewCreated: 눌렀다", )
            activityViewModel.nullPicture()
            activityViewModel.receiveServerPictureData(it.uid)
            fragmentViewModel.myShowLocalData(it)
            fragmentViewModel.changeStartGap("local")
        }
    }

    override fun initObserver() {
        //서버에서 받아온 데이터 옵져버
        activityViewModel.serverData.observe(viewLifecycleOwner){
            serverAdapter.dataList.clear()
            serverAdapter.dataList = it
            setTextAndVisible("데이터가 비었어요!", serverAdapter.dataList.isEmpty())
        }

        //룸에서 받아온 데이터 옵져버
        activityViewModel.roomData.observe(viewLifecycleOwner){
            localAdapter.dataList.clear()
            localAdapter.dataList = it.map {it.toRecyclerShowData()}.toMutableList()
            setTextAndVisible("데이터가 비었어요!", localAdapter.dataList.isEmpty())
        }

        //프로그래스 바 컨트롤
        activityViewModel.connectedState.observe(viewLifecycleOwner){
            Log.e("TAG", "progress bar: $it", )
            if(it==ConnectedState.DISCONNECTED) {
                binding.progressBar.visibility = View.INVISIBLE
            }
            else if(it==ConnectedState.CONNECTING){
                binding.progressBar.visibility = View.VISIBLE
            }
            else if(it==ConnectedState.CONNECTING_SUCCESS){
                setTextAndVisible("", false)
            }else{
                binding.progressBar.visibility = View.INVISIBLE
                setTextAndVisible("서버 연결 실패!", true)
            }
        }

        activityViewModel.picture.observe(viewLifecycleOwner){
            Log.e("TAG", "받아온 사진: $it", )
            if(it!=null) findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("TAG", "onViewCreated: ", )
        super.onViewCreated(view, savedInstanceState)
        //서버에서 사진데이터를 받아왔을 경우 showPage로 넘기기.


        //뒤로가기 버튼
        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    fun initServerRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = serverAdapter
        serverAdapter.dataList.clear()
    }
    fun initLocalRecyclerView(){
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = localAdapter
        localAdapter.dataList.clear()
    }

    fun setTextAndVisible(text:String, state:Boolean){
        binding.backgroundText.text = text
        binding.backgroundText.isVisible = state
    }


    override fun onResume() {
        super.onResume()
        serverAdapter.dataList.clear()
        localAdapter.dataList.clear()
    }

    /** Fragment 뒤로가기 **/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.e("TAG", "handleOnBackPressed: 뒤로가기", )
                if (activityViewModel.connectedState.value == ConnectedState.CONNECTING) {
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