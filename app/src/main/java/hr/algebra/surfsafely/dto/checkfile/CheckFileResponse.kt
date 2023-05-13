package hr.algebra.surfsafely.dto.checkfile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckFileResponse(
    val meta: Meta,
    val data: Data
)

@JsonClass(generateAdapter = true)
data class Meta(
    val file_info: FileInfo
)

@JsonClass(generateAdapter = true)
data class FileInfo(
    val sha256: String,
    val sha1: String,
    val md5: String,
    val size: Int
)

@JsonClass(generateAdapter = true)
data class Data(
    val attributes: Attributes,
    val type: String,
    val id: String,
    val links: Links
)

@JsonClass(generateAdapter = true)
data class Attributes(
    val date: Long,
    val status: String,
    val stats: Stats,
    val results: Map<String, Any>
)

@JsonClass(generateAdapter = true)
data class Stats(
    val harmless: Int,
    @Json(name = "type-unsupported")
    val type_unsupported: Int,
    val suspicious: Int,
    @Json(name = "confirmed-timeout")
    val confirmed_timeout: Int,
    val timeout: Int,
    val failure: Int,
    val malicious: Int,
    val undetected: Int
)

@JsonClass(generateAdapter = true)
data class Links(
    val item: String,
    val self: String
)

