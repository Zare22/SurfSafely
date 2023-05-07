package hr.algebra.surfsafely.dto.checkfile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckFileResponse(
    val data: FileData,
    val type: String,
    val id: String,
    val links: Links
)

@JsonClass(generateAdapter = true)
data class FileData(
    val attributes: FileAttributes
)

@JsonClass(generateAdapter = true)
data class FileAttributes(
    val type_description: String,
    val tlsh: String,
    val type_tags: List<String>,
    val names: List<String>,
    val last_modification_date: Long,
    val type_tag: String,
    val times_submitted: Int,
    val total_votes: TotalVotes,
    val size: Int,
    val type_extension: String,
    val last_submission_date: Long,
    val last_analysis_results: Map<String, AnalysisResult>,
    val sha256: String,
    val tags: List<String>,
    val last_analysis_date: Long,
    val unique_sources: Int,
    val first_submission_date: Long,
    val ssdeep: String,
    val md5: String,
    val sha1: String,
    val magic: String,
    val last_analysis_stats: LastAnalysisStats,
    val meaningful_name: String,
    val reputation: Int
)

@JsonClass(generateAdapter = true)
data class TotalVotes(
    val harmless: Int,
    val malicious: Int
)

@JsonClass(generateAdapter = true)
data class AnalysisResult(
    val category: String,
    val engine_name: String,
    val engine_version: String,
    val result: String?,
    val method: String,
    val engine_update: String
)

@JsonClass(generateAdapter = true)
data class LastAnalysisStats(
    val harmless: Int,
    val type_unsupported: Int,
    val suspicious: Int,
    val confirmed_timeout: Int,
    val timeout: Int,
    val failure: Int,
    val malicious: Int,
    val undetected: Int
)

@JsonClass(generateAdapter = true)
data class Links(
    val self: String
)

