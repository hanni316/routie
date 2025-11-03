import retrofit2.http.Body
import retrofit2.http.POST

interface WalkSessionApi {
    @POST("/walk-sessions/start")
    suspend fun startWalkSession(
        @Body body: WalkSessionStartRequestDto
    ): WalkSessionStartResponseDto

    @POST("/walk-sessions/end")
    suspend fun endWalkSession(
        @Body body: WalkSessionEndRequestDto
    ): WalkSessionEndResponseDto
}