package com.example.receiptcareapp.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.ui.adapter.HomeCardAdapter
import com.example.receiptcareapp.ui.adapter.PermissionHandler
import com.example.receiptcareapp.ui.botteomSheet.CardBottomSheet
import com.example.receiptcareapp.ui.dialog.AddDialog
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.fragmentViewModel.HomeViewModel
import com.example.receiptcareapp.ui.dialog.PermissiondCheck_Dialog
import com.example.receiptcareapp.viewModel.dialogViewModel.HomeCardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, "HomeFragment") {

    private val viewModel : HomeViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback
    private val homeCardBottomSheet: CardBottomSheet by lazy { CardBottomSheet(viewModel) }
    private val adapter: HomeCardAdapter = HomeCardAdapter()
    private val addDialog : AddDialog = AddDialog()
    private val permissiondcheckDialog = PermissiondCheck_Dialog()
    private val ALL_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val handler = Handler(Looper.getMainLooper())


    override fun initData() {

    }

    override fun initUI() {
        //카드목록, 공지사항 불러오기
        viewModel.getServerCardData()
        viewModel.getNoticeList()

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 권한이 허용되었는지 확인
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                // 권한이 허용되었을 때, 권한이 필요한 작업 수행
                addDialog.show(parentFragmentManager, "addDialog")
            } else {
                showShortToast("필수 권한을 허용해주세요!")
                handler.postDelayed({ permissiondcheckDialog.show(parentFragmentManager,"permissiondcheckDialog") }, 800)

            }
        }

    }

    override fun initListener() {
        with(binding){
            historyBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_recyclerFragment) }
            menuBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }
            noticeBtn.setOnClickListener{ findNavController().navigate(R.id.action_homeFragment_to_noticeFragment) }
            addBtn.setOnClickListener{
                if (!checkAllPermissionsGranted(ALL_PERMISSIONS)) {
                    permissionLauncher.launch(ALL_PERMISSIONS)
                }
                else {
                    addDialog.show(parentFragmentManager, "addDialog")
                }
            }
            cardListBtn.setOnClickListener{
                homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet")
            }
            homeCardRecyclerview.setOnClickListener{ homeCardBottomSheet.show(parentFragmentManager,"homeCardBottomSheet") }
            homeCardRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            homeCardRecyclerview.adapter = adapter
        }
    }

    override fun initObserver() {
        //프로그래스 바 컨트롤
        viewModel.loading.observe(viewLifecycleOwner){
            if(it) binding.layoutLoadingProgress.root.visibility = View.VISIBLE
            else binding.layoutLoadingProgress.root.visibility = View.INVISIBLE
        }

        viewModel.cardList.observe(viewLifecycleOwner) { dataList ->
            if (dataList?.body!!.isEmpty()) { binding.emptyText.visibility = View.VISIBLE }
            else {
                adapter.dataList = dataList.body!!.toMutableList()
                binding.emptyText.visibility = View.INVISIBLE
            }
        }

        viewModel.notice.observe(viewLifecycleOwner){
            binding.homeNoticeTxt.text = it
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireActivity(), R.style.AppCompatAlertDialog)
                    .setTitle("종료")
                    .setMessage("꿀을 그만 빠시겠어요?")
                    .setPositiveButton("그만 빤다"){dialog, id->
                        requireActivity().finish()
                    }
                    .setNegativeButton("더 빤다"){dialog, id->

                    }.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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