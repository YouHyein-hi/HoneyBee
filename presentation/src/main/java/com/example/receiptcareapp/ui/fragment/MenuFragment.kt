package com.example.receiptcareapp.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.ui.activity.LoginActivity
import com.example.receiptcareapp.ui.botteomSheet.HomeCardBottomSheet
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate, "MenuFragment") {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val homeCardBottomSheet: HomeCardBottomSheet = HomeCardBottomSheet()
    private var isNotificationEnabled = true


    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {


        binding.menuBackBtn.setOnClickListener { findNavController().popBackStack() }

        binding.cardListBtn.setOnClickListener {
            homeCardBottomSheet.show(parentFragmentManager,"tag")
        }

        binding.noticeBtn.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_noticeFragment) }

        binding.licenseBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
        }

        binding.logoutBtn.setOnClickListener {
            activityViewModel.clearAll()
            activity?.finish()
            Toast.makeText(requireContext(), "로그아웃 성공.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))

        }

//        binding.switchOnoff.isChecked = isNotificationEnabled
//        binding.switchOnoff.setOnCheckedChangeListener { _, isChecked ->
//            isNotificationEnabled = isChecked
//
//            if (isChecked) {
//                // 푸시 알림을 켜는 작업
//                enablePushNotifications()
//            }
//            else {
//                // 푸시 알림을 끄는 작업
//                disablePushNotifications()
//            }
//        }

    }

    override fun initObserver() {
    }

    // 푸시 알림을 켜는 작업
    private fun enablePushNotifications() {
    }
    // 푸시 알림을 끄는 작업
    private fun disablePushNotifications() {
    }
}