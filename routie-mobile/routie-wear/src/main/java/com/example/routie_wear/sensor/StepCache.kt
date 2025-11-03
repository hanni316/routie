package com.example.routie_wear.sensor

/**
 * 워치의 마지막 알려진 총 걸음수(누적)를 유지하는 싱글톤.
 * Sensor.TYPE_STEP_COUNTER에서 받은 값을 항상 최신으로 덮어쓴다.
 * WorkoutTimerScreen 등 UI에서는 여기서 값을 읽어서
 * "세션 시작 시 스냅샷", "세션 종료 시 스냅샷"으로 사용한다.
 */
object StepCache {
    // 오늘 하루 누적 걸음수 같은 개념 (혹은 디바이스 부팅 이후 누적)
    // Sensor.TYPE_STEP_COUNTER에서 event.values[0]로 얻는 값 그대로 저장
    var lastKnownTotalSteps: Int = 0
        internal set
}