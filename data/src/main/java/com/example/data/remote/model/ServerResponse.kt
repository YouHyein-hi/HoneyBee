import com.example.data.remote.model.*
import com.example.domain.model.receive.*
import com.example.domain.util.changeDate

/**
 * 2023-08-19
 * pureum
 */
data class ServerResponse<T>(
    val status: String,
    val message: String,
    val     body: T? = null
)

fun <T: List<ServerCardResponse>> ServerResponse<T>.toServerCardData() = ServerCardData(status, message, body?.map { it.toCardData() })

fun ServerResponse<String>.toServerResponseData() = ServerResponseData(status, message, body!!)

fun ServerResponse<List<ServerBillResponse>>.toServerBillData():ServerBillData = ServerBillData(status, message, body?.map { it.toBillData() } )


fun ServerResponse<List<ServerNoticeResponse>>.toServerNoticeData() = ServerNoticeData(status, message, body?.map { it.toNoticeData() })

fun ServerResponse<Int>.toUidServerResponseData() = ServerUidData(status, message, body)

