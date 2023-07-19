package com.example.receiptcareapp.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * 2023-01-31
 * pureum
 */

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate:Inflate<VB>,
    name: String
) : Fragment() {

    val name = name

    private var _binding: VB? = null
    val binding get() = _binding!!


    // lifeCycle 1
    // 가장 먼저 호출
    override fun onAttach(context: Context) {
        Log.e(name, "fragment onAttach", )
        super.onAttach(context)
    }

    // fragment가 생성된 단계
    // 세로, 가로 모드로 변환 시 oncreate 함수가 다시 호출됨
    // 이때 해당 프래그먼트의 값들은 모두 초기화되기 때문에
    // 값을 유지하고 싶을 경우 savedInstanceState 를 이용하는게 좋음.
    // 리소스들을 초기화해주는 단계,
    // 데이터 관련 친구들여기서 관리
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(name, "fragment onCreate: ", )
        super.onCreate(savedInstanceState)
        initData()
    }

    //레이아웃을 inflate 하는 단계
    // view 객체를 얻을 수 있어 view와 관련된 초기화 진행 가능.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(name, "fragment onCreateView: ", )
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
        //null 케이즈
    }

    //view 반환을 보장받는 위치
    // 확정적으로 view 보장받을 수 있는 단계
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e(name, "fragment onViewCreated: ", )
        initUI()
        super.onViewCreated(view, savedInstanceState)
    }

    // 사용자랑 상호작용하기 직전에 호출단계
    override fun onResume() {
        Log.e(name, "fragment onResume: ", )
        super.onResume()
        initListener()
        initObserver()
    }

    abstract fun initData()
    abstract fun initUI()
    abstract fun initListener()
    abstract fun initObserver()

    //viewBinding으로 인한 메모리 누수 방지
    override fun onDestroyView() {
        Log.e(name, "fragment onDestroyView: ", )
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.e(name, "fragment onDestroy: ", )
        super.onDestroy()
    }

    override fun onDetach() {
        Log.e(name, "fragment onDetach: ", )
        super.onDetach()
    }

    fun showShortToast(message: String?){
        context?.let {
            Toast.makeText(it, message ?: "", Toast.LENGTH_SHORT).show()
        }
    }

    fun showLongToast(message: String?){
        context?.let {
            Toast.makeText(it, message ?: "", Toast.LENGTH_LONG).show()
        }
    }
//    companion object{
//        var TAG = ""
//    }
}