data class WalkSessionEndRequestDto(
    val sessionId: Long,
    val endStepCount: Int,
    val durationMillis: Long
)