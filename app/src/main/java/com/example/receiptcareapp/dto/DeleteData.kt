package com.example.receiptcareapp.dto

import com.example.domain.model.DomainReceiveAllData
import com.example.domain.model.DomainRecyclerData
import com.example.domain.model.DomainRoomData
import com.example.receiptcareapp.State.DeleteType

/**
 * 2023-03-21
 * pureum
 */
data class DeleteData(
    var type : DeleteType,
    var data : DomainRecyclerData?
)
