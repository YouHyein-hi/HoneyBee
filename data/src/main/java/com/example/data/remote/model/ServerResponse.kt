/**
 * 2023-08-19
 * pureum
 */
data class ServerResponse<T>(
    val status: String,
    val message: String,
    val body: T? = null
)



