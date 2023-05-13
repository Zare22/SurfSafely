package hr.algebra.surfsafely.dto.checkurl

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckUrlResponse(
    val matches: List<Match>?
)

@JsonClass(generateAdapter = true)
data class Match(
    val cacheDuration: String,
    val platformType: String,
    val threat: Threat,
    val threatEntryType: String,
    val threatType: String
)

@JsonClass(generateAdapter = true)
data class Threat(
    val url: String
)
