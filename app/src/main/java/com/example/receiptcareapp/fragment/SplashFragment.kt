package com.example.receiptcareapp.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentSplashBinding
import com.example.receiptcareapp.fragment.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch() {
            delay(2300L)
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

    //애니메이션
    override fun onResume() {
        Log.e("TAG", "onResume: ", )
        super.onResume()
        ObjectAnimator.ofFloat(this.binding.title, View.ALPHA, 0f,1f).apply {
            duration = 1500
            start()
        }
        ObjectAnimator.ofFloat(this.binding.background, View.ALPHA, 0f,1f).apply {
            duration = 1500
            start()
        }
    }
}