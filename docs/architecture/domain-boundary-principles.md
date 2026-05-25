# 도메인 경계 설계 원칙

이 문서는 Commerce Lab에서 도메인, Aggregate, JPA 연관관계, 트랜잭션 경계를 판단할 때 사용할 기준을 정리합니다.

## 1. Aggregate 판단 기준

Aggregate는 객체 묶음이 아니라 강한 일관성을 지켜야 하는 트랜잭션 경계입니다.

같은 Aggregate 후보로 볼 수 있는 경우는 다음과 같습니다.

- 같은 트랜잭션 안에서 반드시 함께 변경되어야 합니다.
- 한쪽이 없으면 다른 쪽이 독립적으로 의미를 갖기 어렵습니다.
- 외부에서 하위 객체를 직접 수정하면 도메인 규칙이 깨집니다.
- 루트 객체가 하위 객체의 불변 조건을 지켜야 합니다.

분리된 Aggregate 후보로 볼 수 있는 경우는 다음과 같습니다.

- 변경 빈도와 동시성 특성이 크게 다릅니다.
- 실패, 재시도, 보상 처리가 별도로 필요합니다.
- 나중에 독립 서비스로 분리될 가능성이 높습니다.
- 단순 조회 편의 때문에 함께 묶고 싶을 뿐입니다.

## 2. Product와 Inventory 분리

`product`는 상품 카탈로그를 관리합니다.

- 상품명
- 상품 설명
- 기본 판매가
- 판매 상태

`inventory`는 재고 정합성을 관리합니다.

- 재고 등록
- 재고 예약
- 재고 차감
- 재고 복구
- 동시성 제어

상품 정보는 낮은 빈도로 변경되고, 재고는 주문 과정에서 높은 빈도로 변경됩니다. 또한 재고는 Redis 선점, DB 락 비교, 예약 만료, 보상 처리 같은 별도 실험 대상입니다.

따라서 Commerce Lab에서는 `product`와 `inventory`를 같은 JPA 연관관계나 cascade로 묶지 않고, 별도 도메인 경계로 다룹니다.

## 3. JPA 연관관계 사용 기준

JPA 연관관계는 같은 Aggregate 내부에서만 우선 사용합니다.

예를 들어 `Order`와 `OrderItem`은 같은 생명주기를 가지므로 같은 Aggregate로 볼 수 있습니다. 이 경우 `@OneToMany`와 cascade 사용을 검토할 수 있습니다.

반면 `Product`와 `Inventory`, `Order`와 `Payment`처럼 서로 다른 도메인 경계를 갖는 대상은 직접 JPA 연관관계로 묶지 않습니다. 필요한 경우 식별자만 저장합니다.

```text
inventory.product_id -> product.id
payment.order_id -> order.id
```

조회 API에서 여러 도메인의 데이터를 함께 보여줘야 하는 경우에는 도메인 모델을 억지로 조립하지 않고, 조회 전용 query model을 사용할 수 있습니다.

## 4. 트랜잭션 경계

트랜잭션 경계는 persistence adapter가 아니라 application service에서 정합니다.

트랜잭션은 DB 기능이지만, 트랜잭션 범위는 유스케이스의 일관성 범위이기 때문입니다.

```text
input adapter -> input port/use case -> application service -> output port -> output adapter
```

기본 원칙은 다음과 같습니다.

- command use case의 트랜잭션은 application service 메서드 단위로 잡습니다.
- persistence adapter는 현재 트랜잭션에 참여합니다.
- 외부 PG 호출, Kafka 발행, Redis 호출을 DB 트랜잭션 안에 넣을지는 유스케이스별로 명시적으로 판단합니다.
- 장기적으로 외부 시스템 연동과 이벤트 발행은 outbox/event 기반 분리를 검토합니다.

## 5. Product 생성과 Inventory 생성

상품 생성과 초기 재고 등록은 항상 같은 책임이라고 단정하지 않습니다.

초기 구현에서는 상품 등록과 재고 등록을 분리합니다.

```text
POST /products
POST /inventories
```

나중에 한 번에 처리해야 하는 요구가 명확해지면, `RegisterSellableProductUseCase` 같은 상위 유스케이스에서 product와 inventory를 조율합니다.

이때도 product가 inventory의 내부 구현을 직접 의존하지 않고, 명시적인 port/use case 또는 이벤트를 통해 연결합니다.
