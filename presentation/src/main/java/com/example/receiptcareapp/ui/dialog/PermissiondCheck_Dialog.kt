package com.example.receiptcareapp.ui.dialog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogPermissionCheckBinding

class PermissiondCheck_Dialog : BaseDialog<DialogPermissionCheckBinding>(DialogPermissionCheckBinding::inflate)  {

    override fun initData() {
    }

    override fun initUI() {
    }

    override fun initListener() {
        with(binding){
            permissionCheckCancelBtn.setOnClickListener{
                dismiss()
            }

            permissionCheckOkBtn.setOnClickListener{
                Log.e("TAG", "initListener: checkBtnPositive 클릭", )
                val packageName = requireContext().packageName
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)

                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Log.e("TAG", "Error opening application settings", e)
                }
                dismiss()

            }

        }
    }

    override fun initObserver() {}

}
