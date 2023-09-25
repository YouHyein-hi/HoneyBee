package com.example.receiptcareapp.ui.dialog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.receiptcareapp.base.BaseDialog
import com.example.receiptcareapp.databinding.DialogPermissionCheckBinding

class PermissionCheckDialog : BaseDialog<DialogPermissionCheckBinding>(DialogPermissionCheckBinding::inflate)  {

    override fun initData() {}

    override fun initUI() {}

    override fun initListener() {
        with(binding){
            permissionCheckCancelBtn.setOnClickListener{
                dismiss()
            }

            permissionCheckOkBtn.setOnClickListener{
                val packageName = requireContext().packageName
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)

                try { startActivity(intent) }
                catch (e: ActivityNotFoundException) { }
                dismiss()
            }
        }
    }

    override fun initObserver() {}

}
