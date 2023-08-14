package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
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
import com.example.receiptcareapp.ui.fragment.record.RecordLocalFragment
import com.example.receiptcareapp.ui.fragment.record.RecordServerFragment
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecordFrameFragment : BaseFragment<FragmentRecordFrameBinding>(FragmentRecordFrameBinding::inflate, "RecordFrame") {
    init { Log.e("TAG", ": RecyclerFragment RecyclerFragment RecyclerFragment", ) }

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: RecordViewModel by viewModels()
    private val recordServerAdapter: RecordServerAdapter = RecordServerAdapter()
    private val recordLocalAdapter: RecordLocalAdapter = RecordLocalAdapter()
    private lateinit var callback : OnBackPressedCallback
    private lateinit var myData : RecyclerData

    override fun initData() {
//        Log.e(TAG, "initData", )
//        viewModel.changeStartGap(ShowType.SERVER)
        //init
//        activityViewModel.changeNullPicture()
        // 통신연결, 서버상태 값 초기화
//        activityViewModel.changeConnectedState(ConnectedState.DISCONNECTED)
        //넘겨받는 데이터의 값을 초기화시키기.
//        activityViewModel.changeSelectedData(null)

        //어뎁터 데이터 리스트 비워주기
//        recordServerAdapter.dataList.clear()
//        recordLocalAdapter.dataList.clear()
    }

    override fun initUI() {
        Log.e(TAG, "initUI", )
        changeFragment(RecordServerFragment(viewModel))
//        if(viewModel.startGap.value == ShowType.LOCAL){
//            Log.e("TAG", "로컬부분!", )
//            binding.bottomNavigationView.menu.findItem(R.id.local).isChecked = true
////            initLocalRecyclerView()
////            activityViewModel.getLocalAllBillData()
//            binding.explain.text = "휴대폰의 데이터 입니다."
//        }
//        else {
//            Log.e("TAG", "서버부분!", )
////            activityViewModel.getServerAllbillData()
////            initServerRecyclerView()
//            binding.explain.text = "서버의 데이터 입니다."
//            activityViewModel.changeNullPicture()
//        }
    }

    override fun initListener() {
        // 하단 바텀시트 버튼
        binding.bottomNavigationView.setOnItemSelectedListener{
            parentFragmentManager.beginTransaction()
            when(it.itemId){
                R.id.server -> {
                    changeFragment(RecordServerFragment(viewModel))
//                    binding.explain.text = "서버의 데이터 입니다."
//                    activityViewModel.getServerAllbillData()
//                    initServerRecyclerView()
                    true
                }
                R.id.local -> {
                    changeFragment(RecordLocalFragment(viewModel))
//                    Log.e("TAG", "click listener local: ", )
//                    binding.explain.text = "휴대폰의 데이터 입니다."
//                    activityViewModel.hideServerCoroutineStop()
//                    setTextAndVisible("",false)
//                    activityViewModel.getLocalAllBillData()
//                    initLocalRecyclerView()
                    true
                }
                else -> {true}
            }
        }

//        //서버 목록에서 리스트를 누를 경우
//        recordServerAdapter.onServerSaveClick = {
//            Log.e("TAG", "initListener: server", )
//            myData = RecyclerData(
//                type = ShowType.SERVER,
//                uid = it.uid,
//                cardName = it.cardName,
//                amount = it.amount,
//                billSubmitTime = it.date,
//                storeName = it.storeName,
//                file = null
//            )
//            activityViewModel.changeSelectedData(myData)
//
//            activityViewModel.changeNullPicture()
//            activityViewModel.getServerPictureData(it.uid)
//
//            viewModel.changeStartGap(ShowType.SERVER)
//        }
//
//        //로컬 목록에서 리스트를 누를경우
//        recordLocalAdapter.onLocalSaveClic = {
//            Log.e("TAG", "initListener: local", )
//            myData = RecyclerData(
//                type = ShowType.LOCAL,
//                uid = it.uid,
//                cardName = it.cardName,
//                amount = it.amount,
//                billSubmitTime = it.date,
//                storeName = it.storeName,
//                file = it.file
//            )
//            activityViewModel.changeSelectedData(myData)
//            activityViewModel.changeNullPicture()
//            viewModel.changeStartGap(ShowType.LOCAL)
//            findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
//        }

        //뒤로가기 버튼
        binding.baseComponent.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun initObserver() {
//        //서버에서 받아온 데이터 옵져버
//        activityViewModel.serverData.observe(viewLifecycleOwner){
//            recordServerAdapter.dataList.clear()
//            recordServerAdapter.dataList = it
//            setTextAndVisible("데이터가 비었어요!", recordServerAdapter.dataList.isEmpty())
//        }
//
//        //룸에서 받아온 데이터 옵져버
//        activityViewModel.roomData.observe(viewLifecycleOwner){
//            recordLocalAdapter.dataList.clear()
//            recordLocalAdapter.dataList = it.map {it.toRecyclerShowData()}.toMutableList()
//            setTextAndVisible("데이터가 비었어요!", recordLocalAdapter.dataList.isEmpty())
//        }

        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }
        //프로그래스 바 컨트롤
//        activityViewModel.connectedState.observe(viewLifecycleOwner){
//            Log.e("TAG", "progress bar: $it", )
//            if(it==ConnectedState.DISCONNECTED) { binding.progressBar.visibility = View.INVISIBLE }
//            else if(it==ConnectedState.CONNECTING){ binding.progressBar.visibility = View.VISIBLE }
//            else if(it==ConnectedState.CONNECTING_SUCCESS){ setTextAndVisible("", false) }
//            else{
//                binding.progressBar.visibility = View.INVISIBLE
//                setTextAndVisible("서버 연결 실패!", true)
//            }
//        }

//        //화면 전환
//        activityViewModel.picture.observe(viewLifecycleOwner){
//            Log.e(TAG, "받아온 사진: $it", )
//            if(it != null){
//                // 서버통신을 통해 받아온 사진 추가
//                activityViewModel.putPictureData(viewModel.bitmapToUri(requireActivity(),it))
//                findNavController().navigate(R.id.action_recyclerFragment_to_recyclerShowFragment)
//            }
//        }
    }

    fun changeFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id,fragment).commit()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

//    fun initServerRecyclerView(){
//        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
//        binding.mainRecycler.adapter = recordServerAdapter
//        recordServerAdapter.dataList.clear()
//    }
//    fun initLocalRecyclerView(){
//        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
//        binding.mainRecycler.adapter = recordLocalAdapter
//        recordLocalAdapter.dataList.clear()
//    }

//    fun setTextAndVisible(text:String, state:Boolean){
//        binding.backgroundText.text = text
//        binding.backgroundText.isVisible = state
//    }

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