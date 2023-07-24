package com.example.receiptcareapp.ui.fragment

import android.content.Intent
import android.widget.Toast
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

class MenuFragment : BaseFragment<FragmentMenuBinding>(
    FragmentMenuBinding::inflate, "MenuFragment"
) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val homeCardBottomSheet: HomeCardBottomSheet = HomeCardBottomSheet()


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

    }

    override fun initObserver() {
    }

}