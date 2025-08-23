# ConJam Backend API

KOPIS(공연예술통합전산망) Open API를 활용한 공연 정보 제공 백엔드 서비스입니다.

## 🚀 빠른 시작

### 1. 환경변수 설정

프로젝트 루트에 `.env` 파일을 생성하고 다음 내용을 입력하세요:

```bash
# .env 파일 내용
KOPIS_API_KEY=your-actual-kopis-api-key
KOPIS_BASE_URL=http://www.kopis.or.kr/openApi/restful
```

또는 시스템 환경변수로 설정할 수 있습니다:

**macOS/Linux:**
```bash
export KOPIS_API_KEY=your-actual-kopis-api-key
export KOPIS_BASE_URL=http://www.kopis.or.kr/openApi/restful
```

**Windows:**
```cmd
set KOPIS_API_KEY=your-actual-kopis-api-key
set KOPIS_BASE_URL=http://www.kopis.or.kr/openApi/restful
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 3. API 테스트

서버가 실행되면 다음 URL에서 API를 테스트할 수 있습니다:

- **헬스체크**: http://localhost:8080/api/performances/health
- **공연 목록**: http://localhost:8080/api/performances?page=1&size=10
- **공연 상세**: http://localhost:8080/api/performances/{공연ID}

## 📋 API 엔드포인트

### 공연 목록 조회
```
GET /api/performances
```

**파라미터:**
- `page`: 페이지 번호 (기본값: 1)
- `size`: 페이지 크기 (기본값: 10, 최대: 100)
- `genre`: 장르 (연극, 뮤지컬, 클래식, 국악, 대중음악, 무용, 복합, 서커스/마술)
- `area`: 지역 코드 (11:서울, 26:부산, 27:대구, 28:인천 등)
- `startDate`: 공연 시작일 (yyyyMMdd)
- `endDate`: 공연 종료일 (yyyyMMdd)

### 공연 상세 조회
```
GET /api/performances/{performanceId}
```

### 헬스체크
```
GET /api/performances/health
```

## 🔐 보안

- API 키와 같은 민감한 정보는 환경변수로 관리됩니다
- `.env` 파일은 `.gitignore`에 포함되어 Git에 커밋되지 않습니다
- 실제 API 키는 절대 코드에 하드코딩하지 마세요

## 🛠 기술 스택

- **Language**: Kotlin
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Gradle
- **Database**: H2 (개발용)
- **HTTP Client**: WebClient (Spring WebFlux)
- **XML Parsing**: Jackson XML

## 📝 KOPIS API 키 발급

1. [KOPIS 홈페이지](http://www.kopis.or.kr) 방문
2. 회원가입 및 로그인
3. 마이페이지 > Open API 신청
4. API 키 발급받기

## 🌐 배포

프로덕션 환경에서는 다음과 같이 환경변수를 설정하세요:

```bash
# 프로덕션 환경변수
export KOPIS_API_KEY=your-production-api-key
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8080
```
