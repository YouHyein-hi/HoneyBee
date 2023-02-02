package com.example.receiptcareapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.receiptcareapp.R
import com.example.receiptcareapp.databinding.FragmentHomeBinding
import com.example.receiptcareapp.fragment.base.BaseFragment

//메인 프레그먼트
//class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
class HomeFragment : Fragment() {

    private val binding : FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.camaraBtn.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_cameraFragment) }
        binding.galleryBtn.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_galleryFragment) }
        binding.historyBtn.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_recyclerFragment) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}