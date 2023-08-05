package com.example.receiptcareapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.ui.activity.LoginActivity
import com.example.receiptcareapp.ui.adapter.PushReceiver
import com.example.receiptcareapp.ui.botteomSheet.HomeCardBottomSheet
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.MenuViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate, "MenuFragment") {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val menuViewModel: MenuViewModel by activityViewModels()
    companion object { const val REQUEST_CODE = 101 }


    override fun initData() {
    }

    override fun initUI() {
    }

    @SuppressLint("MissingPermission")
    override fun initListener() {


        binding.menuBackBtn.setOnClickListener { findNavController().popBackStack() }

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

        binding.pushSwitch.isChecked = menuViewModel.getPush()!!
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = Intent(requireContext(), PushReceiver::class.java).let {
            it.putExtra("code", REQUEST_CODE)
            it.putExtra("count", 10)
            PendingIntent.getBroadcast(
                requireContext(),
                REQUEST_CODE,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        binding.pushSwitch.setOnCheckedChangeListener { _, isChecked ->
            menuViewModel.putPush(isChecked)
            if (isChecked) {

                val targetTime = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 16)
                    set(Calendar.MINUTE, 35)
                }
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    targetTime.timeInMillis,
                    pendingIntent
                )


                showShortToast("푸시 알림 ON")
            } else {
                alarmManager.cancel(pendingIntent)
                showShortToast("푸시 알림 OFF")
            }
        }

    }

    override fun initObserver() {
    }

}