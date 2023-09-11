package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.ui.adapter.HomeCardAdapter
import com.example.receiptcareapp.util.PermissionHandler
import com.example.receiptcareapp.ui.botteomSheet.CardBottomSheet
import com.example.receiptcareapp.ui.dialog.AddDialog
import com.example.receiptcareapp.ui.dialog.ExitDialog
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.fragmentViewModel.HomeViewModel
import com.example.receiptcareapp.ui.dialog.PermissiondCheck_Dialog
import com.example.receiptcareapp.state.FetchState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, "HomeFragment") {
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback
    private val adapter: HomeCardAdapter = HomeCardAdapter()
    private val ALL_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val handler = Handler(Looper.getMainLooper())


    override fun initData() {
        adapter.dataList.clear()
    }

    override fun initUI() {
        //카드목록, 공지사항 불러오기
        viewModel.getServerCardData()
        viewModel.getNoticeList()
        initHomeCardRecycler()
        viewModel.initFetchState()

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 권한이 허용되었는지 확인
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                // 권한이 허용되었을 때, 권한이 필요한 작업 수행
                addDialog()
            } else {
                showShortToast("필수 권한을 허용해주세요!")
                handler.postDelayed({ permissiondcheckDialog() }, 800)
            }
        }
    }

    override fun initListener() {
        with(binding){
            homeHistoryBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment) }
            homeMenuBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }
            homeNoticeComponent.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_noticeFragment) }
            homeAddBtn.setOnClickListener{
                if (!checkAllPermissionsGranted(ALL_PERMISSIONS)) { permissionLauncher.launch(ALL_PERMISSIONS) }
                else { addDialog() }
            }
            homeCardListComponent.setOnClickListener { CardBottomSheet(viewModel).show(parentFragmentManager, "homeCardBottomSheet") }
            homeCardRecyclerviewRefresh.setOnRefreshListener {
                homeCardRecyclerviewRefresh.isRefreshing = false
                viewModel.getServerCardData()
            }
        }
    }

    override fun initObserver() {
        //프로그래스 바 컨트롤
        //TODO databinding
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }


        viewModel.cardList.observe(viewLifecycleOwner) { dataList ->
            if (dataList?.body!!.isEmpty()) { emptyTextControl(true) }
            else {
                adapter.dataList = dataList.body!!.toMutableList()
                emptyTextControl(false)
            }
        }


        viewModel.notice.observe(viewLifecycleOwner){
            binding.notice = it
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            when(it.second){
//                FetchState.SOCKET_TIMEOUT_EXCEPTION -> { emptyTextControl(true, "서버 연결 실패..") }
//                FetchState.PARSE_ERROR -> {emptyTextControl(true, "서버 연결 실패..")}
            }
            showShortToast(FetchStateHandler(it))
            adapter.dataList.clear()
        }
    }

    private fun initHomeCardRecycler(){
        binding.homeCardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.homeCardRecyclerview.adapter = adapter
        adapter.dataList.clear()
    }

    private fun emptyTextControl(state: Boolean, massage: String = "카드를 추가해주세요!"){
        binding.homeEmptyTxt.isVisible = state
        binding.homeEmptyTxt.text = massage
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun addDialog(){
        val addDialog = AddDialog()
        addDialog.show(parentFragmentManager, "addDialog")
    }

    private fun permissiondcheckDialog(){
        val permissiondcheckDialog = PermissiondCheck_Dialog()
        permissiondcheckDialog.show(parentFragmentManager, "permissiondcheckDialog")
    }

    private fun exitDialog(){
        val exitDialog = ExitDialog()
        exitDialog.show(parentFragmentManager, "exitDialog")
    }


    private fun checkAllPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.e("TAG", "onRequestPermissionsResult: 에 접근",)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}