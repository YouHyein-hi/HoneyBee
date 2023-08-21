package com.example.receiptcareapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.ui.activity.LoginActivity
import com.example.receiptcareapp.ui.adapter.PushReceiver
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.MenuViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate, "MenuFragment") {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: MenuViewModel by viewModels()
    companion object { const val REQUEST_CODE = 101 }
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
        binding.pushTime.text = viewModel.timePickerText(
            viewModel.getTime().hour!!,
            viewModel.getTime().minute!!
        )
    }

    @SuppressLint("MissingPermission", "ResourceAsColor")
    override fun initListener() {
        with(binding){
            menuBackBtn.setOnClickListener { findNavController().popBackStack() }

            noticeBtn.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_noticeFragment) }

            licenseBtn.setOnClickListener {
                startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
            }

            logoutBtn.setOnClickListener {
                activityViewModel.clearAll()
                activity?.finish()
                Toast.makeText(requireContext(), "로그아웃 성공.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }

            pushTimeButton.setOnClickListener{
                val cal = Calendar.getInstance()
                val data = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    viewModel.putTime(hour, minute)

                    if (pushSwitch.isChecked) {
                        setAlarm() // Switch가 켜져있다면 알람 설정
                    }
                }

                val timeDialog = TimePickerDialog(requireContext(), data,
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
                timeDialog.show()
                timeDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.sub_font4_orange2))
                timeDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }


            pushSwitch.isChecked = viewModel.getPush()!!
            pushSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.putPush(isChecked)
                if (isChecked) {
                    setAlarm()
                    showShortToast("푸시 알림 ON")
                } else {
                    cancelAlarm()
                    showShortToast("푸시 알림 OFF")
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.pushTime.observe(viewLifecycleOwner) { pushTime ->
            binding.pushTime.text = viewModel.timePickerText(pushTime.hour!!, pushTime.minute!!)
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }


    private fun setAlarm() {
        val intervalDays = listOf(-5, -3, -1, 0, 1, 3, 5) // -5, -3, -1, 0, +1, +3, +5일 간격 리스트
        val targetDate = Calendar.getInstance().apply {
            // 여기에 원하는 날짜 설정
            set(Calendar.YEAR, get(Calendar.YEAR))
            set(Calendar.MONTH, get(Calendar.MONTH)) // 0부터 시작하므로 7은 8월을 나타냄
            set(Calendar.DAY_OF_MONTH, 21)
            set(Calendar.HOUR_OF_DAY, viewModel.getTime().hour!!)
            set(Calendar.MINUTE, viewModel.getTime().minute!!)
        }

        for (intervalDay in intervalDays) {
            val targetTime = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                timeInMillis = targetDate.timeInMillis // 설정한 날짜로 변경
                add(Calendar.DAY_OF_MONTH, intervalDay)
            }

            // 정확한 시간에 알람 예약
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                targetTime.timeInMillis,
                pendingIntent
            )

            // 다음 알람을 위해 intervalDay만큼 시간 증가
            targetDate.add(Calendar.DAY_OF_MONTH, intervalDay)
        }
    }

    private fun cancelAlarm() {
        alarmManager.cancel(pendingIntent)
    }


}