//package com.example.receiptcareapp.ui.fragment
//
//import android.animation.ObjectAnimator
//import android.view.View
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import com.example.receiptcareapp.R
//import com.example.receiptcareapp.databinding.FragmentSplashBinding
//import com.example.receiptcareapp.base.BaseFragment
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
//
//    override fun initData() {}
//
//    override fun initUI() {
//        ObjectAnimator.ofFloat(this.binding.title, View.ALPHA, 0f,1f).apply {
//            duration = 1500
//            start()
//        }
//        ObjectAnimator.ofFloat(this.binding.background, View.ALPHA, 0f,1f).apply {
//            duration = 1500
//            start()
//        }
//
//        lifecycleScope.launch() {
//            delay(2300L)
//            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
//        }
//    }
//
//    override fun initListener() {}
//
//    override fun initObserver() {}
//}