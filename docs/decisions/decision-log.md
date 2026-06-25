# Decision Log

Commerce Lab의 주요 설계 결정을 간단히 기록합니다. 장기 영향이 크거나 대안 비교가 필요한 결정은 별도 ADR로 승격합니다.

## 2026-05-25

### Product와 Inventory는 별도 도메인 경계로 둔다

- 결정: `product`와 `inventory`를 같은 JPA 연관관계나 cascade 저장 대상으로 묶지 않는다.
- 이유: product는 상품 카탈로그, inventory는 재고 정합성과 동시성 제어를 책임진다. 변경 빈도, 실패 처리, 향후 서비스 분리 가능성이 다르다.
- 결과: inventory는 product를 객체 연관관계가 아니라 `product_id` 식별자로 참조한다.

### 트랜잭션 경계는 application service에서 정한다

- 결정: command use case의 트랜잭션 경계는 application service에 둔다.
- 이유: 트랜잭션 범위는 DB 작업 단위가 아니라 유스케이스의 일관성 범위다.
- 결과: persistence adapter는 트랜잭션을 결정하지 않고 현재 트랜잭션에 참여한다.

### JPA 연관관계는 Aggregate 내부에 우선 제한한다

- 결정: 서로 다른 도메인 경계 사이에서는 JPA cascade와 객체 연관관계를 기본으로 사용하지 않는다.
- 이유: 조회 편의 때문에 도메인 생명주기를 묶으면 나중에 재고, 결제, 주문의 독립적 변경과 분리가 어려워진다.
- 결과: 도메인 간 참조는 식별자, port/use case, event, query model을 우선 사용한다.

## 2026-06-23

### 로컬 개발 DB는 단일 MySQL 인스턴스로 시작한다

- 결정: Modular Monolith 단계에서는 애플리케이션이 하나의 MySQL datasource에 연결한다.
- 이유: 현재 목표는 도메인 경계와 핵심 주문 흐름을 먼저 검증하는 것이다. 도메인별 물리 DB 분리는 MSA 전환 실험 단계에서 다룬다.
- 결과: DB 접속 설정은 `app`의 `local` 프로필에 두고, 각 도메인은 자기 `adapter.output.persistence` 안에서 자신이 소유한 테이블만 접근한다.
- ADR 후보: 도메인별 DB 분리 시점과 Outbox/Saga 도입 기준.

### 로컬 관측 환경은 Prometheus와 Grafana로 구성한다

- 결정: 로컬 개발 환경에 MySQL, Prometheus, Grafana를 Docker Compose로 함께 둔다.
- 이유: 주문, 재고, 결제 흐름의 장애와 병목을 학습하려면 애플리케이션 메트릭을 개발 초기부터 수집하고 확인할 수 있어야 한다.
- 결과: Spring Boot Actuator의 Prometheus endpoint를 열고, Prometheus는 해당 endpoint를 scrape하며, Grafana는 Prometheus datasource를 자동 등록한다.

## 2026-06-25

### 1차 주문 구현에서는 Member 도메인을 만들지 않는다

- 결정: 주문 도메인 1차 구현에서는 `member` 도메인과 `orders.member_id` FK를 만들지 않는다.
- 이유: 현재 우선순위는 주문-재고-결제 핵심 흐름 검증이다. 회원 가입, 인증, 주소록까지 먼저 만들면 주문 도메인 설계 범위가 불필요하게 커진다.
- 결과: `orders`에는 `orderer_id`, `orderer_name`, `orderer_email`을 주문 당시 스냅샷으로 저장한다. 향후 `member` 또는 `customer` 도메인이 생기면 nullable FK 추가를 검토하되, 주문 당시 스냅샷은 유지한다.

### 주문 항목은 Product와 JPA 연관관계로 묶지 않는다

- 결정: `order_item.product_id`는 상품 식별자와 주문 당시 상품 스냅샷을 저장하며, `Product` JPA Entity와 객체 연관관계로 묶지 않는다.
- 이유: 주문은 이력 데이터이고 상품명, 가격은 주문 당시 값으로 보존되어야 한다. 상품 도메인의 현재 상태 변경이 기존 주문 항목에 전파되면 안 된다.
- 결과: 주문 항목은 `product_id`, `product_name`, `unit_price`, `quantity`, `line_amount`를 가진다.

### 주문 생성 요청에는 Idempotency Key를 사용할 수 있게 한다

- 결정: 주문 생성 중복 방지를 위해 `orderer_id`와 `idempotency_key` 조합을 요청 식별자로 사용할 수 있게 설계한다.
- 이유: 주문 생성은 부작용이 있는 command이므로 더블클릭, timeout 재시도, 클라이언트 retry로 같은 주문 의도가 중복 생성될 수 있다.
- 결과: 클라이언트는 주문 시도 단위로 UUID 같은 키를 생성하고, 서버는 `orderer_id + idempotency_key` 조합으로 기존 주문을 조회한다. DB unique 제약도 이 조합을 기준으로 검토한다.
