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
