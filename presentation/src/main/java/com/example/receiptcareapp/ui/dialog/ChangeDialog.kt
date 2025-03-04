package com.example.receiptcareapp.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.data.util.UriToBitmapUtil
import com.example.domain.model.remote.receive.card.CardSpinnerData
import com.example.domain.model.remote.send.bill.SendBillUpdateData
import com.example.domain.model.ui.dateTime.DateData
import com.example.domain.model.ui.type.ShowType
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogChangeBinding
import com.example.domain.model.ui.bill.LocalBillData
import com.example.domain.model.ui.recycler.RecyclerData
import com.example.domain.util.StringUtil
import com.example.receiptcareapp.R
import com.example.receiptcareapp.ui.adapter.SpinnerAdapter
import com.example.receiptcareapp.state.FetchState
import com.example.receiptcareapp.util.FetchStateHandler
import com.example.receiptcareapp.viewModel.activityViewmodel.MainActivityViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.GalleryViewModel
import com.example.receiptcareapp.viewModel.fragmentViewModel.record.RecordShowViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ChangeDialog(
    private val viewModel: RecordShowViewModel
) : BaseDialog<DialogChangeBinding>(DialogChangeBinding::inflate) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val galleryViewModel : GalleryViewModel by viewModels()
    private lateinit var viewModelData: RecyclerData
    private var cardName = ""
    private var cardId = 0
    private var myCardName = ""
    private var imageUri: Uri? = null
    private lateinit var dateData : DateData
    private var newDate = listOf<String>()
    private var cardDataList: MutableList<CardSpinnerData> = mutableListOf()
    private var cardArrayList = ArrayList<CardSpinnerData>()


    override fun initData() {
        if (activityViewModel.selectedData.value != null) {
            viewModelData = activityViewModel.selectedData.value!!
            Log.e("TAG", "initData: ${activityViewModel.selectedData.value}", )
            Log.e("TAG", "initData: ${viewModelData}", )
            newDate = StringUtil.dateReplaceDot(viewModelData.date)
            Log.e("TAG", "initData: ${newDate}", )
        } else {
            showShortToast("데이터가 없습니다!")
            dismiss()
        }

        viewModel.getServerCardData()

        try {
            dateData = DateData(
                year = newDate[0].toInt(),
                month = newDate[1].toInt(),
                day = newDate[2].toInt()
            )
        } catch (e: NullPointerException) {
            dismiss()
            showShortToast("날짜 불러오기를 실패했습니다.")
        }

        viewModel.textValue = viewModelData.amount
        myCardName = viewModelData.cardName
        imageUri = viewModelData.file
    }

    override fun initUI() {
        binding.data = viewModelData
        binding.changePriceEdit.setText(viewModel.textValue)
        binding.changeDateDatePicker.init(dateData.year, dateData.month - 1, dateData.day, null)
    }

    override fun initListener() {

        binding.changeImage.setOnLongClickListener {
            Log.e("TAG", "initListener: 그림 길게 클릭함!", )
            CallGallery()
            return@setOnLongClickListener(true)
        }

        binding.changeCardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCardData = cardDataList[position]
                cardId = position
                cardName = selectedCardData.name
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.changeOkBtn.setOnClickListener {
            dateData = DateData(
                year = binding.changeDateDatePicker.year,
                month = binding.changeDateDatePicker.month + 1,
                day = binding.changeDateDatePicker.dayOfMonth
            )

            val myLocalDateTime = StringUtil.myLocalDateTimeFuntion(dateData.year, dateData.month, dateData.day)
            val price = binding.changePriceEdit.text.toString()
            val priceZero = price.count { it == '0' }
            when {
                cardName == "" -> { showShortToast("카드를 입력하세요.") }
                binding.changeStoreEdit.text!!.isEmpty() -> { showShortToast("가게 이름을 입력하세요.") }
                binding.changePriceEdit.text!!.isEmpty() -> { showShortToast("금액을 입력하세요.") }
                priceZero == price.length -> { showShortToast("금액에 0원은 입력이 안됩니다.") }
                myLocalDateTime.toString() == "" -> { showShortToast("날짜를 입력하세요.") }
                else -> {
                    if (viewModelData.type == ShowType.SERVER) {
                        viewModel.updateServerBillData(
                            SendBillUpdateData(
                                id = viewModelData.uid.toLong(),
                                billSubmitTime = myLocalDateTime!!,
                                storeAmount = binding.changePriceEdit.text.toString().replace(",", "").toInt(),
                                cardName = cardName,
                                storeName = binding.changeStoreEdit.text.toString(),
                                billCheck = false,
                                billMemo = binding.changeMemoEditText.text.toString()
                            )
                        )
                    } else {
                        viewModel.updateLocalBillData(
                            sendData = LocalBillData(
                                uid= viewModelData.uid,
                                date = myLocalDateTime.toString(),
                                storeAmount = binding.changePriceEdit.text.toString(),
                                cardName = cardName,
                                storeName = binding.changeStoreEdit.text.toString(),
                                picture = viewModelData.file!!,
                                memo = binding.changeMemoEditText.text.toString()
                            )
                        )
                    }
                    dismiss()
                }
            }
        }
        binding.changeCancelBtn.setOnClickListener { dismiss() }

    }

    override fun initObserver() {

        viewModel.cardList.observe(viewLifecycleOwner){
            if (it?.body?.isEmpty() == true) {
                dismiss()
            } else {
                cardDataList.clear()
                it?.body?.let { it1 -> cardDataList.addAll(it1) }
                cardArrayList.clear()
                cardArrayList.addAll(cardDataList)
                val adapter = SpinnerAdapter(requireContext(), cardArrayList)
                binding.changeCardSpinner.adapter = adapter

                val position = StringUtil.findPositionByCardName(myCardName, adapter)
                if (position != -1) {
                    binding.changeCardSpinner.setSelection(position)
                } else {
                    showShortToast("카드 불러오기 실패!")
                    Log.e("TAG", "initObserver: $position")
                }
            }
        }

        // Err관리
        viewModel.fetchState.observe(this) {
            when (it.second) {
                FetchState.SOCKET_TIMEOUT_EXCEPTION -> dismiss()
            }
            showShortToast(FetchStateHandler(it))
        }

        // 이렇게 말고 show 부분에서 picture bitmap이나 url 가져오면 되는 거 아닌가? (핳)
        viewModel.changePicture.observe(viewLifecycleOwner){
            Glide.with(binding.changeImage)
                .load(it)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(binding.changeImage)
//            checkImageData()
        }
        viewModel.image.observe(viewLifecycleOwner){
            imageUri = it
        }
    }

    fun CallGallery() {
        Log.e("TAG", "CallGallery 실행", )
        val galleryIntent = galleryViewModel.CallGallery()
        activityResult.launch(galleryIntent)
    }

    /* 갤러리 사진 관련 함수 */
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { it ->
        if (it.resultCode == Activity.RESULT_OK){
            val data: Intent? = it.data
            Log.e("TAG", "onActivityResult: if 진입", )
            imageUri = it.data?.data
            if (imageUri != null) {
                Log.e("TAG", "data 있음", )
                val bitmap : Bitmap? = UriToBitmapUtil.uriToBitmap(requireContext(), imageUri!!)
                val rotatedBitmap = UriToBitmapUtil.rotateImageIfRequiredUri(requireContext(), imageUri!!, bitmap!!)
                rotatedBitmap?.let { it -> viewModel.takeChangePicture(it) }
                viewModelData.file = imageUri as Uri
            }
            else{ Log.e("TAG", "data 없음", ) }
        }
        else{
            Log.e("TAG", "RESULT_OK if: else 진입", )
        }
    }


/*    private fun checkImageData(){
        if(binding.changeImageImageView.drawable == null)
            binding.changeEmptyTxt.isVisible = true
        if(viewModel.picture.value==null)
            binding.changeEmptyTxt.isVisible = true
    }*/

}