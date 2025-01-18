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
Language | ![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white) ![HTML5](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
IDE | ![intellij-idea](https://img.shields.io/badge/intellij%20idea-000000?style=for-the-badge&logo=intellijidea&logoColor=white) 
Framework | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
Build Tool | ![gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
Database | ![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)
Library | ![Spring Security](https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=jpa&logoColor=white) ![Java Mail](https://img.shields.io/badge/Java%20Mail-3a75b0?style=for-the-badge) ![apache kafka](https://img.shields.io/badge/apache%20kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
Tools | ![git](https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white) ![notion](https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white)  

## 📁 아키텍처
<img src="https://github.com/user-attachments/assets/617a1c69-1c2f-46cd-8cbd-da2bdacd1c73" width=800px>

## ⭐ 성능최적화

### [조회 성능 개선](https://github.com/project-fcfs/fcfs/wiki/%EC%A1%B0%ED%9A%8C-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0)

| **항목**                     | **캐시 사용 전** | **캐시 사용 후** | **차이**           | **퍼센트 변화**       |
|-----------------------------|----------------|-----------------|--------------------|-----------------------|
| **Http_req_duration(mean)**  | 3.39s          | 2.63s           | 0.76s 개선         | 22.42% 개선          |
| **Http_req_duration (min)**  | 5.47ms         | 0.80ms          | 4.67ms 개선        | 85.38% 개선
| **Max Response Time**        | 25.2s          | 22.7s           | 2.5s 개선          | 9.93% 개선           |
| **Average Response Time**    | 5.26s          | 2.98s           | 2.28s 개선         | 43.36% 개선          |
| **Peak RPS**                 | 1562           | 1908            | 346 요청 증가      | 22.12% 증가          |
| **TPS (Transactions per second)** | 703          | 1096            | 393 처리  증가  | 55.92% 증가          |

### [주문 동시성 비교](https://github.com/project-fcfs/fcfs/wiki/%EC%A3%BC%EB%AC%B8-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%84%B1%EB%8A%A5-%EB%B9%84%EA%B5%90)
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
| **상황**                        | **원인**                                                                                                     | **해결책**                                                                                                 |
|---------------------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| **JWT 토큰 에러**                | Jwt 예외는 Security 흐름과 별개로 처리되기 때문에 잡지 못함                                                   | 예외가 발생하면 로그인 실패 처리만 하면 됨                                                                  |
| **분산락 교착상태**              | 락 획득, 해제에 같은 트랜잭션으로 할 경우 리소스 잠금으로 데드락 발생                                           | 트랜잭션과 락을 분리해서 사용. 락을 먼저 처리한 후 트랜잭션 진행                                             |
| **Cors**                        | 기본적으로 Cors는 모든 헤더를 보여주지 않음                                                                   | 원하는 헤더를 Cors에 추가하여 보여줌                                                                         |
| **CacheManager**                | `GenericJackson2JsonRedisSerializer()`로 직렬화 시 패키지 이름까지 포함하여 직렬화                                 | `Jackson2JsonRedisSerializer<Object>(Object.class)`를 사용하여 데이터만 직렬화 진행                          |
| **GateWay Rate Limiter**        | Gateway는 WebFlux 기반이므로 동기적 Redis(Lettuce) 사용 시 예외 발생                                             | ReactiveRedis를 사용. Redis 사용하지 않을 경우에도 `RequestRateLimiterGatewayFilterFactory`가 자동으로 적용 가능 |
