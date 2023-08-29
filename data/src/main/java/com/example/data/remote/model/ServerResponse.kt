import com.example.data.remote.model.*
import com.example.domain.model.receive.*
import com.example.domain.model.receive.bill.ServerBillData
import com.example.domain.model.receive.bill.ServerStoreData
import com.example.domain.model.receive.card.ServerCardData
import com.example.domain.model.receive.card.ServerCardSpinnerData
import com.example.domain.model.receive.notice.ServerNoticeData

/**
 * 2023-08-19
 * pureum
 */
data class ServerResponse<T>(
    val status: String,
    val message: String,
    val body: T? = null
)



