-- Product domain schema
--
-- 설계 메모
-- - id: 내부 조인과 외래키 참조에 사용하는 데이터베이스 기본 키입니다.
-- - status: 주문 이력이 생긴 상품을 물리 삭제하지 않고 판매 가능 여부를 제어하기 위한 상태입니다.
-- - 재고 수량은 inventory 도메인에서 관리하므로 product 테이블에 두지 않습니다.

CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '내부 조인과 외래키 참조에 사용하는 상품 기본 키',
    name VARCHAR(200) NOT NULL COMMENT '상품명',
    description VARCHAR(1000) NULL COMMENT '상품 설명',
    price BIGINT NOT NULL COMMENT '상품 판매가. KRW 기준 원 단위 금액',
    status VARCHAR(32) NOT NULL COMMENT '상품 판매 상태. ACTIVE는 판매 가능, DISCONTINUED는 판매 중지',
    created_at DATETIME(6) NOT NULL COMMENT '상품 생성 일시',
    updated_at DATETIME(6) NOT NULL COMMENT '상품 최종 수정 일시',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='상품 카탈로그 테이블. 재고 수량은 inventory 도메인에서 관리한다.';

CREATE INDEX idx_product_status ON product (status);
