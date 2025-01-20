## 🛒 PICK ME - 유통기한이 그리 길지 않은 제품들 값싸게 살 수 있는 서비스
<img src="https://github.com/user-attachments/assets/0647c6bf-7d01-4395-9755-49edab96f526" width=800>
<img src="https://github.com/user-attachments/assets/5a0584e3-8b20-46bc-80e5-ae214906decb" width=800>

### 🔍어떤 서비스 인가요?
**PICK ME**는 유통기한이 임박한 식재료를 저렴하게 제공하는 플랫폼입니다. 이 서비스는 환경 보호와 소비자의 경제적 부담 완화를 목표로 하며, 고객들에게 신선하고 품질 좋은 식재료를 합리적인 가격에 제공합니다. 매일 새로운 한정 수량의 제품을 업데이트해 소비자들에게 다양한 선택과 혜택을 제공합니다. 이를 통해 소비자들은 질 좋은 식재료를 낭비 없이 활용할 수 있으며, 환경 보호에도 동참할 수 있습니다.

### 🤔 어떤 문제를 해결하려고 하나요?
**PICK ME** 서비스는 유통기한이 임박한 식재료의 낭비를 줄이고, 소비자들에게 저렴한 가격에 신선한 제품을 제공하려는 문제를 해결하고자 합니다.

1. 식재료 낭비 문제: 유통기한이 임박한 식재료는 종종 버려지게 되는데, 이를 효과적으로 판매하여 낭비를 줄이고, 소비자에게 합리적인 가격으로 제공함으로써 환경에 미치는 영향을 최소화합니다.

2. 비싼 식재료 가격 문제: 많은 소비자들이 고품질의 신선한 식재료를 비싼 가격에 구매하고 있습니다. **PICK ME**는 유통기한이 짧은 제품을 저렴하게 제공하여, 가격 부담을 줄이고 경제적인 쇼핑을 가능하게 합니다.

3. 빠르게 변화하는 시장의 요구: 소비자들이 신선하고 질 좋은 식재료를 빠르게 구매할 수 있도록 선착순 구매와 매일 새롭게 업데이트되는 혜택을 제공하여, 빠르게 변화하는 시장의 요구를 충족시키고 있습니다.


## 🚀 기술 스택

OS | Stack
--- | --- |
Language | ![Java21](https://img.shields.io/badge/java%2021-007396?style=for-the-badge&logo=java&logoColor=white) ![HTML5](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
IDE | ![intellij-idea](https://img.shields.io/badge/intellij%20idea-000000?style=for-the-badge&logo=intellijidea&logoColor=white) 
Framework | ![Spring Boot3.4](https://img.shields.io/badge/Spring%20Boot%203.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
Build Tool | ![gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
Database | ![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)
Library | ![Spring Security](https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=jpa&logoColor=white) ![Java Mail](https://img.shields.io/badge/Java%20Mail-3a75b0?style=for-the-badge) ![apache kafka](https://img.shields.io/badge/apache%20kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
Tools | ![git](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white) ![notion](https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white)  

## 📁 아키텍처
<img src="https://github.com/user-attachments/assets/617a1c69-1c2f-46cd-8cbd-da2bdacd1c73" width=800px>

## 🔨 주요 기능
- MSA기반으로 서비스 독립성과 확장성 향상
- Eureka 서비스 디스커버리와 API Gateway를 활용한 동적 서비스 등록 및 라우팅 구현
- OpenFeign을 통한 외부 모듈 통신, Resilience4j Circuit Breaker와 Retry로 회복 탄력성 강화
- Redis를 이용한 캐싱 처리로 서비스 성능 최적화
- Redis와 Lua Script를 이용한 재고 감소 설계로 원자적 동시성 처리
- Kafka를 통한 이벤트 기반 처리로 안정적인 트랜잭션 관리 및 실패 보상(Choreography SAGA)
- Docker Compose로 컨테이너 기반의 통합 개발/배포 환경 구성
- Google SMTP로 이메일 인증 구현

### Sequence Diagram
<img src="https://github.com/user-attachments/assets/92843000-69f6-4186-b8c1-671c60ee4aa6" width=500>

## ⭐ 성능최적화
### [Rate Limiter](https://github.com/project-fcfs/fcfs/wiki/Rate-Limiter)
```
- 요청이 많을 때 서비스의 성능 저하를 방지하고, 자원을 효율적으로 분배하여 안정성을 높이고자 했다.
- 평균 응답 시간이 5~6초로 느린 상태를 개선하고, 빠르고 일관된 응답 속도를 제공하기 위해 Rate Limiter를 고려했다.
- 구글 리서치 자료에 의하면 응답시간이 5초이상 넘어가면 90% 이탈한다고 한다
```
<img src="https://github.com/user-attachments/assets/b0e983c5-c7a3-4813-a0f9-7df5c475e631" width=500>

### [조회 성능 개선](https://github.com/project-fcfs/fcfs/wiki/%EC%A1%B0%ED%9A%8C-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0)
```
- 기본 키 인덱스를 활용하여 효율적인 조회가 이루어졌지만, 고도화된 성능을 요구하는 환경에서는 여전히 한계가 존재했댜
- 사용자가 자주 사용하는 메인페이지의 경우, 빈번한 조회로 인해 데이터베이스의 부하가 증가하였고, 캐싱이나 최적화되지 않은 쿼리로 인해 성능 저하가 발생할 수 있었다
- MSA 특성상 스케일 아웃으로 인한 여러 인스턴스가 존재할 수 있어 하나의 캐시서버가 필요했다
```

| **항목**                     | **캐시 사용 전** | **캐시 사용 후** | **차이**           | **퍼센트 변화**       |
|-----------------------------|----------------|-----------------|--------------------|-----------------------|
| **Http_req_duration(mean)**  | 3.39s          | 2.63s           | 0.76s 개선         | 22.42% 개선          |
| **Http_req_duration (min)**  | 5.47ms         | 0.80ms          | 4.67ms 개선        | 85.38% 개선
| **Max Response Time**        | 25.2s          | 22.7s           | 2.5s 개선          | 9.93% 개선           |
| **Average Response Time**    | 5.26s          | 2.98s           | 2.28s 개선         | 43.36% 개선          |
| **Peak RPS**                 | 1562           | 1908            | 346 요청 증가      | 22.12% 증가          |
| **TPS (Transactions per second)** | 703          | 1096            | 393 처리  증가  | 55.92% 증가          |

### [주문 동시성 비교](https://github.com/project-fcfs/fcfs/wiki/%EC%A3%BC%EB%AC%B8-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%84%B1%EB%8A%A5-%EB%B9%84%EA%B5%90)
```
- 여러 종류의 락 중에서 예상 시나리오에 가장 적합한 락을 검토했다.  
- 성능 면에서 비관적 락과 LuaScript가 가장 유리하다고 판단했다.  
- LuaScript를 활용해 원자적 연산을 처리하고, 순차적인 주문이 가능하도록 구현했다.  
- Redis에서 수정된 재고는 Kafka를 활용한 Write-Through 방식으로 관리했다.  
```
<img src="https://github.com/user-attachments/assets/69f23676-013f-4cd8-9959-05c8639977fd" width=1000>

### [스레드 개수 성능 비교](https://github.com/project-fcfs/fcfs/wiki/%EC%93%B0%EB%A0%88%EB%93%9C-%EA%B0%9C%EC%88%98%EC%97%90-%EB%94%B0%EB%A5%B8-%EC%84%B1%EB%8A%A5-%EB%B3%80%ED%99%94)
| **항목**                     | **커넥션 5개** | **커넥션 13개** | **커넥션 25개** | **커넥션 100개** | **5개 → 13개** | **13개 → 25개** | **25개 → 100개** |
|-----------------------------|---------------|----------------|----------------|-----------------|----------------|-----------------|------------------|
| **CPU 사용량**               | 0.51          | 0.541          | 0.568          | 0.665           | 6.00% 증가      | 5.00% 증가      | 17.12% 증가      |      |
| **Http_req_duration(avg)**   | 435.53ms      | 452.21ms       | 393.86ms       | 353.6ms         | 3.83% 증가      | 12.89% 감소     | 10.22% 감소      |
| **Http_req_duration(min)**   | 9.78ms        | 11.36ms        | 9.99ms         | 9.88ms          | 16.15% 증가     | 12.09% 감소     | 1.11% 감소       |
| **Http_req_duration(max)**   | 3.7s          | 3.22s          | 2.28s          | 1.27s           | 13.02% 감소     | 29.15% 감소     | 44.64% 감소      | 
| **TPS (Transactions per second)** | 237         | 218            | 250            | 279             | -8.02% 감소     | 14.71% 증가     | 11.60% 증가      

## 🐞 [Trouble Shooting](https://github.com/project-fcfs/fcfs/wiki/%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)
| **상황**            | **해결책**                                                                                                  |
|---------------------|-------------------------------------------------------------------------------------------------------------|
| **JWT 토큰 에러**   | 로그인 실패 시 예외 원인을 로깅하여 디버깅에 활용                                                           |
| **분산락 교착상태** | 트랜잭션과 락을 분리하여 처리. 락을 먼저 처리한 후 트랜잭션 진행                                            |
| **CORS Header**            | 필요한 헤더를 CORS 설정에 추가하여 요청 허용                                                               |
| **CacheManager**    | `Jackson2JsonRedisSerializer<Object>(Object.class)`를 사용하여 데이터 직렬화 진행                          |
| **Gateway Rate Limiter** | ReactiveRedis를 사용하여 Redis 기반 Rate Limiter를 적용. Redis 미사용 시 대체 필터 설정                    |


