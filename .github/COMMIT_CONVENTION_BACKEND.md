# 커밋 컨벤션 — Backend

> Spring Boot + Java 기반 프로젝트의 커밋 메시지 규칙입니다.

---

## 커밋 메시지 형식

```
<타입>(<스코프>): <요약>

<본문>

<푸터>
```

### 예시

```
feat(score): SUCPI 지표 산출 로직 구현

교육/연구/교류 활동 가중치 기반으로 성과지수를 계산하는
ScoreCalculationService 구현.

Closes #15
```

```
fix(auth): 만료된 JWT 토큰 재발급 시 NPE 수정
```

```
feat(api)!: 학생 조회 API 응답 구조 변경

BREAKING CHANGE: student 필드가 studentInfo로 변경됨
```

---

## 타입

| 타입 | 설명 |
|------|------|
| `feat` | API 또는 비즈니스 로직에 새로운 기능 추가 또는 변경 |
| `fix` | `feat` 커밋으로 인해 발생한 버그 수정 |
| `refactor` | API/동작 변경 없이 코드 구조 개선 |
| `perf` | 성능 개선 목적의 `refactor` |
| `style` | 코드 포맷팅, 공백, 세미콜론 누락 등 동작에 영향 없는 변경 |
| `test` | 테스트 추가 또는 수정 |
| `docs` | 문서만 변경 |
| `chore` | 패키지 구조 변경, 파일 이동/이름 변경, 버전 수정 등 |
| `revert` | 이전 커밋 되돌리기 |

---

## 어떤 타입을 써야 할지 모르겠다면

| 질문 | Yes → 타입 |
|------|-----------|
| 버그를 수정했나요? | `fix` |
| 기능 추가 또는 API 동작이 바뀌었나요? | `feat` |
| 성능 개선이 목적인가요? | `perf` |
| 코드 구조만 바꿨나요? (동작 변화 없음) | `refactor` |
| 포맷팅, 공백, 세미콜론만 변경했나요? | `style` |
| 테스트를 추가/수정했나요? | `test` |
| 문서만 변경했나요? | `docs` |
| 빌드, 환경설정, Docker 관련인가요? | `chore` |

---

## 스코프

스코프는 변경된 코드 영역을 나타냅니다. **선택사항**이지만 가능하면 명시하세요.

| 스코프 | 설명 |
|--------|------|
| `auth` | 인증/인가 (JWT, 세션) |
| `student` | 학생 정보 관련 |
| `score` | SUCPI 성과지수 산출 로직 |
| `activity` | 학생 활동 데이터 (교육/연구/교류) |
| `admin` | 관리자 기능 |
| `db` | 데이터베이스 마이그레이션, 엔티티 변경 |
| `api` | REST API 컨트롤러 |
| `service` | 서비스 레이어 |
| `config` | Spring 설정, 보안 설정 등 |
| `docker` | Docker, docker-compose 관련 |
| `ci` | GitHub Actions, CI/CD 파이프라인 |

---

## 요약 (Description)

- **필수** 항목입니다
- 현재형 명령조로 작성: "추가한다" ✅ / "추가했다" ❌
- 첫 글자 소문자 (한국어는 해당 없음)
- 마침표로 끝내지 않기
- **72자 이내**

```
# ✅ Good
feat(score): SUCPI 지표 산출 로직 구현
fix(auth): JWT 토큰 만료 시 자동 갱신 처리 추가

# ❌ Bad
feat(score): SUCPI 지표 산출 로직을 구현했습니다.
Fix NPE
```

---

## 본문 (Body)

- **선택** 항목입니다
- 요약줄과 빈 줄로 구분
- *무엇*을 했는지가 아닌 ***왜*** 했는지를 설명
- 현재형 명령조 사용

---

## 푸터 (Footer)

- **선택** 항목이지만 이슈 참조 시 반드시 작성
- 이슈 참조: `Closes #이슈번호` / `Fixes #이슈번호`
- 브레이킹 체인지: `BREAKING CHANGE:` 로 시작

```
Closes #34

BREAKING CHANGE: /api/v1/students 응답 필드 구조가 변경됨
```

---

## 브레이킹 체인지 (Breaking Changes)

API 응답 구조, 엔드포인트 경로 등 하위 호환성이 깨지는 변경 시 반드시 표시합니다.

```
# 타입 뒤에 ! 추가
feat(api)!: 학생 목록 조회 응답 구조 변경

BREAKING CHANGE: students 배열이 data.students로 중첩 변경됨.
프론트엔드 API 호출부 수정 필요.
```

---

## 브랜치 전략

```
main
└── feature/#이슈번호
```

```bash
# 1. main 최신화
git pull origin main

# 2. 이슈 번호 기반으로 브랜치 생성
git branch feature/#15
git switch feature/#15

# 3. 작업 후 커밋
git add .
git commit -m "feat(score): 교육 활동 가중치 계산 메서드 구현"

# 4. push 전 다시 main 최신화 (충돌 방지)
git pull origin main

# 5. 충돌 해결 후 push
git push origin feature/#15
```

> PR 생성 후 Reviewer가 Merge 진행. **main 직접 push 금지.**

---

## 전체 예시

```
# 기본
feat(activity): 연구 활동 등록 API 구현

# 이슈 참조 포함
fix(db): 학생 삭제 시 연관 활동 데이터 미삭제 오류 수정

Closes #29

# 본문 포함
refactor(service): 성과지수 계산 로직을 도메인 서비스로 이동

기존 컨트롤러 레이어에서 계산하던 로직을
ScoreDomainService로 이동하여 레이어 책임 분리.

# 브레이킹 체인지
feat(api)!: 인증 토큰 헤더 방식 변경

BREAKING CHANGE: X-AUTH-TOKEN 헤더 대신 Authorization: Bearer 방식으로 변경됨

# 초기 커밋
chore: init
```
