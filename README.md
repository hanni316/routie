# Routie

Routie는 사용자가 운동 루틴을 만들고 관리하며, 게임적 요소를 통해 동기를 부여받을 수 있는 안드로이드 기반 피트니스 애플리케이션입니다.

## ✨ 주요 기능

- **나만의 루틴 생성 및 관리**: 사용자가 원하는 운동으로 루틴을 구성하고 관리할 수 있습니다.
- **캐릭터 커스터마이징**: 운동을 통해 얻은 재화로 캐릭터를 꾸밀 수 있습니다.
- **게이미피케이션**: 재화, 뽑기(가챠), 랭킹 시스템 등 게임 요소를 통해 운동에 대한 동기를 부여합니다.
- **삼성 헬스 연동**: 삼성 헬스와 연동하여 운동 데이터를 기록하고 관리합니다.
- **Wear OS 지원**: 모바일 앱과 연동되는 Wear OS용 컴패니언 앱을 제공합니다.

## 📂 프로젝트 구조

```
routie-main/
├── routie-mobile/  # 안드로이드 클라이언트 (모바일, 웨어러블)
└── routie-server/  # 백엔드 서버
```

- **`routie-server`**: Spring Boot 기반의 백엔드 API 서버입니다. 사용자 데이터, 루틴, 랭킹 등 전반적인 데이터를 관리합니다.
- **`routie-mobile`**: 안드로이드 클라이언트 애플리케이션입니다.
  - **`app`**: 메인 안드로이드 애플리케이션 모듈입니다.
  - **`routie-wear`**: Wear OS용 컴패니언 앱 모듈입니다.

## 🛠️ 사용된 기술

### Backend (`routie-server`)

- **Language**: Java
- **Framework**: Spring Boot
- **Build Tool**: Gradle

### Frontend (`routie-mobile`)

- **Platform**: Android, Wear OS
- **Language**: Kotlin, Java
- **SDK**: Samsung Health SDK
- **Build Tool**: Gradle

### CI/CD

- **GitHub Actions**

## 🚀 빌드 및 실행

### Backend

1. `routie-server` 디렉토리로 이동합니다.
2. 다음 명령어를 실행하여 서버를 빌드하고 실행합니다.
   ```bash
   ./gradlew bootRun
   ```

### Frontend

1. `routie-mobile` 디렉토리를 Android Studio에서 엽니다.
2. Gradle 동기화가 완료되면, `app` 모듈을 선택하여 빌드 및 실행합니다.
