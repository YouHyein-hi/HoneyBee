package com.example.receiptcareapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
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

    private var myHour = "0"
    private var myMinute = "0"
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    override fun initData() {
        // AlarmManager와 PendingIntent 초기화
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), PushReceiver::class.java).apply {
            putExtra("code", REQUEST_CODE)
            putExtra("count", 10)
        }
        pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
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

        binding.pushTimeButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val data = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                menuViewModel.putTime(hour, minute)

                updatePushTimeText()

                if (binding.pushSwitch.isChecked) {
                    setAlarm() // Switch가 켜져있다면 알람 설정
                }
            }

            val timeDialog = TimePickerDialog(requireContext(), data,
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timeDialog.show()
            timeDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(Color.RED)
            timeDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.BLACK)
        }


        //05 : 00
        binding.pushSwitch.isChecked = menuViewModel.getPush()!!
        binding.pushSwitch.setOnCheckedChangeListener { _, isChecked ->
            menuViewModel.putPush(isChecked)
            if (isChecked) {
                setAlarm()
                showShortToast("푸시 알림 ON")
            } else {
                cancelAlarm()
                showShortToast("푸시 알림 OFF")
            }
        }

        // 최초 세팅
        updatePushTimeText()

    }

    override fun initObserver() {
    }

    private fun setAlarm() {
        val targetTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, menuViewModel.getTime().hour!!)
            set(Calendar.MINUTE, menuViewModel.getTime().minute!!)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            targetTime.timeInMillis,
            pendingIntent
        )

        updatePushTimeText()
    }

    private fun cancelAlarm() {
        alarmManager.cancel(pendingIntent)
        updatePushTimeText()
    }

    private fun updatePushTimeText() {
        // 이건 format으로 하면 될거같은데~~ 일단 이렇게 하자
        // 그리고 ViewModel에 빼줘야됨~!~!!!
        if (menuViewModel.getTime().hour!! < 10) myHour = "0${menuViewModel.getTime().hour!!}"
        else myHour = "${menuViewModel.getTime().hour!!}"
        if (menuViewModel.getTime().minute!! < 10) myMinute = "0${menuViewModel.getTime().minute!!}"
        else myMinute = "${menuViewModel.getTime().minute!!}"

        binding.pushTime.text = "${myHour} : ${myMinute}"
    }

}