package com.example.receiptcareapp.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.base.BaseFragment
import com.example.receiptcareapp.databinding.FragmentMenuBinding
import com.example.receiptcareapp.ui.activity.LoginActivity
import com.example.receiptcareapp.util.PushReceiver
import com.example.receiptcareapp.ui.dialog.PushTimeDialog
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
        val time = viewModel.getTime()
        binding.menuPushTimeTxt.text = time.let {
            StringUtil.timePickerText(
                it.hour ?: 0,
                it.minute ?: 0
            )
        }
    }

    @SuppressLint("MissingPermission", "ResourceAsColor")
    override fun initListener() {
        with(binding){
            menuBackBtn.setOnClickListener { findNavController().popBackStack() }

            menuNoticeBtn.setOnClickListener { findNavController().navigate(R.id.action_menuFragment_to_noticeFragment) }

            menuLicenseBtn.setOnClickListener {
                startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
            }

            menuLogoutBtn.setOnClickListener {
                viewModel.removeAuth()
                activity?.finish()
                Toast.makeText(requireContext(), "로그아웃 성공.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }

            menuPushTimeBtn.setOnClickListener{
                PushTimeDialog()
            }


            menuPushSwitch.isChecked = viewModel.getPush()!!
            menuPushSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.putPush(isChecked)
                if (isChecked) {
                    checkMaxDayOfMonth()
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
            binding.menuPushTimeTxt.text = pushTime?.let {
                StringUtil.timePickerText(
                    it.hour ?: 0,
                    it.minute ?: 0
                )
            }
        }

        viewModel.fetchState.observe(this) {
            showShortToast(FetchStateHandler(it))
        }
    }

    private fun PushTimeDialog(){
        PushTimeDialog(viewModel) {
            if (binding.menuPushSwitch.isChecked) {
                checkMaxDayOfMonth() // Switch가 켜져있다면 알람 설정
            }
        }.show(parentFragmentManager, "pushTimeDialog")
    }

    private fun checkMaxDayOfMonth() {
        val currentDate = Calendar.getInstance()
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        val maxDayOfMonth = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysBeforeMaxDay = listOf(0,1, 2, 3)

        if (daysBeforeMaxDay.contains(maxDayOfMonth - dayOfMonth)) {
            Log.e("TAG", "MenuFragment : Alarm set on day $dayOfMonth")
            setAlarm()
        } else {
            Log.d("TAG", "MenuFragment : Alarm not set on day $dayOfMonth")
//            setAlarm()
        }
    }

    //TODO 이쪽은 알람 메니저 클래스 or Object 클래스를 만들고 알람기능은 그곳에서 담당하는걸로 뺴주자
    private fun setAlarm(){
        val time = viewModel.getTime()
        val targetTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            time.hour?.let { set(Calendar.HOUR_OF_DAY, it) }
            time.minute?.let { set(Calendar.MINUTE, it) }
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            targetTime.timeInMillis,
            pendingIntent
        )
    }
    private fun cancelAlarm() {
        alarmManager.cancel(pendingIntent)
    }

}