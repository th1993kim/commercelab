# 백엔드 코드 컨벤션

이 문서는 Commerce Lab 백엔드 코드 작성 시 따를 기본 컨벤션을 정의합니다.

## 1. 기본 원칙

- 백엔드 코드는 Kotlin으로 작성합니다.
- Spring Boot 기반으로 구현하되, 프레임워크 편의성보다 도메인 경계와 의존성 방향을 우선합니다.
- 기능을 추가할 때는 주문, 재고, 결제 등 도메인 단위의 책임을 먼저 구분합니다.
- 단순 CRUD 구조가 아니라, 유스케이스와 도메인 규칙이 드러나는 구조를 지향합니다.
- 구현 방식이 애매한 경우 기존 문서, 도메인 책임, 테스트 가능성을 기준으로 결정합니다.

## 2. Kotlin 작성 원칙

- Java 코드를 새로 작성하지 않고 Kotlin을 기본 언어로 사용합니다.
- null 가능성은 Kotlin type system으로 명확히 표현합니다.
- 의미 없는 nullable 타입과 강제 non-null assertion(`!!`) 사용을 피합니다.
- 데이터 전달 목적의 객체는 `data class`를 우선 사용합니다.
- 도메인 상태 변경은 가능한 한 명확한 메서드로 표현합니다.
- 외부 입력 DTO와 도메인 모델을 직접 공유하지 않습니다.

## 3. 아키텍처 원칙

백엔드 코드는 헥사고날 아키텍처를 기준으로 작성합니다.

핵심 원칙은 다음과 같습니다.

- 도메인 계층은 프레임워크와 외부 시스템에 의존하지 않습니다.
- 애플리케이션 계층은 유스케이스를 표현하고 트랜잭션 경계를 담당합니다.
- 포트는 애플리케이션 계층에 두고, 어댑터가 포트를 구현합니다.
- 웹, DB, Redis, Kafka, PG 연동 코드는 어댑터 계층에 둡니다.
- 의존성 방향은 항상 `adapter -> application -> domain`을 따릅니다.

## 4. 권장 패키지 구조

도메인별로 패키지를 나누고, 각 도메인 내부에서 헥사고날 구조를 유지합니다.

```text
com.commercelab
 └── order
     ├── domain
     │   ├── model
     │   ├── event
     │   └── service
     ├── application
     │   ├── port
     │   │   ├── input
     │   │   └── output
     │   └── service
     └── adapter
         ├── input
         │   ├── web
         │   └── messaging
         └── output
             ├── persistence
             ├── redis
             └── external
```

Kotlin 키워드와 충돌을 피하기 위해 패키지명에는 `in`, `out` 대신 `input`, `output`을 사용합니다.

## 5. 계층별 역할

### 5.1 Domain

- 엔티티, 값 객체, 도메인 이벤트, 도메인 서비스를 둡니다.
- Spring, JPA, Redis, Kafka 관련 어노테이션을 두지 않습니다.
- 도메인 규칙과 상태 전이는 도메인 계층에 둡니다.

### 5.2 Application

- 유스케이스를 표현하는 input port를 둡니다.
- 외부 저장소나 외부 시스템이 필요한 작업은 output port로 추상화합니다.
- 애플리케이션 서비스는 input port를 구현합니다.
- 트랜잭션 경계는 원칙적으로 애플리케이션 서비스에 둡니다.

### 5.3 Adapter

- HTTP API, Kafka consumer, DB persistence, Redis, 외부 PG 연동을 둡니다.
- input adapter는 외부 요청을 애플리케이션 input port 호출로 변환합니다.
- output adapter는 애플리케이션 output port를 구현합니다.
- 어댑터 내부의 DTO, Entity, 외부 API 모델은 도메인 모델과 분리합니다.

## 6. 우선 적용 범위

초기 구현에서는 아래 규칙을 우선 적용합니다.

- 모든 백엔드 신규 코드는 Kotlin으로 작성합니다.
- 도메인별 패키지를 먼저 나누고, 내부를 `domain`, `application`, `adapter`로 분리합니다.
- Controller에서 Repository를 직접 호출하지 않습니다.
- Application service에서 JPA Entity를 외부로 노출하지 않습니다.
- Domain model이 JPA Entity나 API DTO에 의존하지 않도록 합니다.

## 7. 데이터베이스 문서화 원칙

- 테이블 DDL 문서에는 테이블, 컬럼, 제약 조건의 의도를 이해할 수 있도록 주석을 남깁니다.
- 테이블 및 컬럼 `COMMENT`는 반드시 한국어로 작성합니다.
- SQL 상단 설계 메모도 한국어로 작성합니다.
- 제약 조건명, 인덱스명, 컬럼명 등 실제 SQL 식별자는 영어 snake_case를 사용합니다.
- DB 제약은 기본 데이터 무결성을 보장하는 범위에서 사용하고, 복잡한 비즈니스 규칙은 도메인 계층에서 표현합니다.
