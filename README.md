# 🏃 Routie

> **App for exercise routine** — 운동 루틴을 만들고, 기록하고, 보상으로 캐릭터를 꾸미는 게이미피케이션 운동 관리 앱

Routie는 사용자가 자신만의 운동 루틴을 설계하고 실천하면서, 운동 기록·걸음 수·소모 칼로리를 추적하고,
그 성과를 게임 요소(가챠, 아이템, 캐릭터 꾸미기, 업적, 랭킹)와 연결해 동기를 부여하는 안드로이드 앱입니다.
스마트워치(Wear OS) 연동을 통해 걸음 수와 건강 데이터를 수집합니다.

## ✨ 주요 기능

- **회원가입 / 로그인** — 사용자 계정 생성 및 인증
- **운동 루틴 관리** — 루틴 생성·수정·삭제, 요일별 스케줄 설정, 루틴에 운동 추가/편집
- **운동 기록 & 로그** — 운동 수행 기록, 루틴 시작/완료 로그 저장
- **건강 데이터 추적** — 걸음 수, 소모 칼로리, 워치 기반 건강 데이터 수집
- **Wear OS 연동** — 스마트워치에서 걸음 수 측정 및 화면 송출
- **상점 & 장바구니** — 아이템 구매, 장바구니 담기, 중복 구매 방지
- **가챠 시스템** — 티켓으로 랜덤 아이템 획득
- **캐릭터 꾸미기 (마이룸)** — 획득한 아이템 장착, 캐릭터 스타일 저장
- **리워드 & 티켓** — 운동 활동에 따른 보상 및 가챠 티켓 지급
- **업적** — 아이템 구매 횟수 등 조건 기반 업적 달성
- **랭킹** — 소모 칼로리 기준 상위 사용자 랭킹(상위 10위) 조회
- **알림** — 시스템 알람을 통한 루틴 리마인더

## 🧩 프로젝트 구조

이 저장소는 **모바일 앱**과 **백엔드 서버**로 구성된 모노레포입니다.
routie/
├── routie-mobile/      # Android 앱 (+ Wear OS 모듈)
│   ├── app/            # 메인 안드로이드 앱
│   └── routie-wear/    # Wear OS(스마트워치) 모듈
└── routie-server/      # Spring Boot 백엔드 서버

### 모바일 (`routie-mobile`)

`com.gbsb.routiemobile` 패키지 아래 화면 단위(Fragment) 기반으로 구성됩니다.

- `fragment/` — 로그인, 회원가입, 메인, 루틴 생성/수정, 마이룸, 상점, 가챠, 랭킹, 프로필, 알람, 설정 등 화면
- `adapter/` — RecyclerView 어댑터
- `api/`, `network/` — 서버 통신(REST API) 레이어
- `dto/` — 데이터 전송 객체
- `viewmodel/` — 화면 상태 관리
- `Health/` — 건강 데이터(걸음 수 등) 처리
- `watch/` — Wear OS 연동
- `notification/` — 시스템 알림

### 서버 (`routie-server`)

`com.gbsb.routie_server` 패키지의 표준 레이어드 아키텍처(controller · service · repository · entity · dto · config · exception)로 구성됩니다.

주요 REST 컨트롤러:

| 도메인 | 컨트롤러 |
| --- | --- |
| 사용자 | `UserController` |
| 루틴 | `RoutineController`, `RoutineDayController`, `RoutineExerciseController`, `RoutineStartController`, `RoutineLogController` |
| 운동 | `ExerciseController`, `ExerciseCategoryController`, `ExerciseLogController`, `CategoryController` |
| 건강 데이터 | `HealthDataController`, `CalorieController`, `WalkSessionController` |
| 상점/아이템 | `ShopController`, `ItemController`, `CartController`, `UserItemController` |
| 게임 요소 | `CharacterController`, `TicketController`, `RewardController`, `AchievementController`, `RankingController` |
| 관리 | `AdminController` |

API 예시 (`RoutineController`, base path `/api/routines`):

| 메서드 | 경로 | 설명 |
| --- | --- | --- |
| `POST` | `/api/routines/{userId}` | 운동 루틴 생성 |
| `GET` | `/api/routines/{userId}` | 사용자 루틴 목록 조회 |
| `GET` | `/api/routines/scheduled` | 특정 날짜의 루틴 조회 |
| `GET` | `/api/routines/detail/{routineId}` | 단일 루틴 상세 조회 |
| `PUT` | `/api/routines/{routineId}` | 루틴 수정 |
| `DELETE` | `/api/routines/{routineId}` | 루틴 삭제 |

## 🛠 기술 스택

**Mobile**
- Kotlin / Java (Android)
- Wear OS

**Server**
- Java 17
- Spring Boot 3.4.2
- Spring Data JPA / Hibernate
- Spring Security
- Spring Validation
- MySQL
- Lombok
- Gradle

## 🚀 시작하기

### 서버 실행

```bash
cd routie-server
# local.properties / application 설정에서 MySQL 접속 정보를 환경에 맞게 구성하세요
./gradlew bootRun
```

> ⚠️ 실행 전 MySQL 데이터베이스를 생성하고 데이터소스(URL/계정/비밀번호)를 설정해야 합니다.

### 모바일 앱 실행

```bash
cd routie-mobile
./gradlew assembleDebug
```

또는 Android Studio에서 `routie-mobile` 프로젝트를 열어 `app` 모듈을 실행합니다.
워치 기능을 사용하려면 `routie-wear` 모듈을 함께 실행하세요.

## 👥 만든 사람들

원본 프로젝트: [yellowsung/routie](https://github.com/yellowsung/routie)
