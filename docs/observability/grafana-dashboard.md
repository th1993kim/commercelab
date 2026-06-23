# Grafana Dashboard Guide

Commerce Lab의 로컬 관측 환경은 Spring Actuator, Prometheus, Grafana로 구성한다.

## 구성 파일

- Spring Actuator 설정: `app/src/main/resources/application.yml`
- Prometheus scrape 설정: `infra/prometheus/prometheus.yml`
- Grafana datasource 설정: `infra/grafana/provisioning/datasources/prometheus.yml`
- Grafana dashboard provider 설정: `infra/grafana/provisioning/dashboards/dashboards.yml`
- 기본 대시보드: `infra/grafana/provisioning/dashboards/commerce-lab-overview.json`

Grafana는 컨테이너 시작 시 provisioning 파일을 읽어 datasource와 dashboard를 자동 등록한다.

## 기본 대시보드

`Commerce Lab Overview` 대시보드는 다음 영역을 우선 본다.

- API: TPS, method/URI/status별 TPS, 4xx/5xx 발생 API path별 분당 실패 수, URL별 분당 요청 수, 10초 이상 slow API path, 평균/최대 latency
- DB: HikariCP active/idle/max/pending connection, connection acquire/usage/timeout
- JVM: heap/non-heap memory, process/system CPU, thread, GC pause

## 지표 추가 방법

### 1. Actuator가 제공하는 기본 지표를 쓰는 경우

Spring Boot와 Micrometer가 이미 노출하는 지표라면 코드 변경 없이 Grafana 패널만 추가한다.

예시 PromQL:

```promql
sum(rate(http_server_requests_seconds_count{application="commercelab"}[5m]))
```

10초 이상 API는 HTTP request histogram/SLO bucket을 사용한다. `le="10.0"` bucket은 10초 이하 요청 수이므로, 전체 요청 수에서 10초 이하 요청 수를 빼서 계산한다.

```promql
sum(rate(http_server_requests_seconds_count{application="commercelab"}[5m]))
-
sum(rate(http_server_requests_seconds_bucket{application="commercelab", le="10.0"}[5m]))
```

4xx/5xx는 전체 TPS보다 어느 path에서 발생했는지를 우선 본다.

```promql
sum by (method, uri, status) (
  rate(http_server_requests_seconds_count{application="commercelab", status=~"4..|5.."}[5m]) * 60
)
```

분당 요청이 많이 들어오는 URL은 상위 10개를 본다.

```promql
topk(10,
  sum by (method, uri) (
    rate(http_server_requests_seconds_count{application="commercelab"}[5m]) * 60
  )
)
```

패널은 `infra/grafana/provisioning/dashboards/commerce-lab-overview.json`에 추가한다.

### 2. 애플리케이션 지표를 직접 추가하는 경우

도메인/유스케이스에서 중요한 이벤트는 Micrometer `MeterRegistry`로 명시적인 지표를 발행한다.

예시:

```kotlin
class OrderMetrics(
    private val meterRegistry: MeterRegistry,
) {
    fun recordOrderCreated() {
        meterRegistry.counter("commerce_order_created_total").increment()
    }
}
```

Prometheus에서는 다음처럼 조회한다.

```promql
rate(commerce_order_created_total[5m])
```

### 3. 지표 이름 원칙

- 애플리케이션 지표는 `commerce_` prefix를 사용한다.
- count 누적값은 `_total` suffix를 사용한다.
- 지표 label에는 높은 cardinality 값을 넣지 않는다.
- 주문 ID, 사용자 ID, 요청 ID 같은 값은 label로 쓰지 않는다.
- 도메인 상태, 결과, 실패 원인처럼 제한된 값만 label로 사용한다.

## 적용 방법

dashboard JSON 또는 provisioning 설정을 변경한 뒤 Grafana를 재시작한다.

```bash
docker compose restart grafana
```

Prometheus scrape 설정을 변경한 뒤에는 Prometheus를 재시작한다.

```bash
docker compose restart prometheus
```
