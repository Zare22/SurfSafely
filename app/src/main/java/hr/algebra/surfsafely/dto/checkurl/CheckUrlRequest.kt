package hr.algebra.surfsafely.dto.checkurl

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckUrlRequest(
    val threatTypes: List<String> = listOf("MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE"),
    val platformTypes: List<String> = listOf("ANY_PLATFORM"),
    val threatEntryTypes: List<String> = listOf("URL"),
    val threatEntries: List<ThreatEntry>
)

@JsonClass(generateAdapter = true)
data class ThreatEntry(val url: String)