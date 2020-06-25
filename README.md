### 환경
- Gradle 6.5
- openjdk version "1.8.0_252"
- spring boot
- jpa hibernate
- h2

## 실행 방법
``` bash
./gradlew bootRun
```
## swagger로 api 테스트
- http://localhost:8080/swagger-ui.html#/

## api

### user api
- 회원 가입 (/api/user/signup)
- ex) curl -X POST "http://localhost:8080/api/user/signup" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\", \"password\": \"123\"}"
### auth api
- jwt를 이용한 토큰 얻기 (/api/auth/create/token)
- ex) curl -X POST "http://localhost:8080/api/auth/create/token" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\", \"password\": \"123\"}"
### coupon api
- header에 accesstoken이 꼭 필요함
- 랜덤한 코드의 쿠폰을 N개 생성 (/api/coupon/create)
ex) curl -X POST "http://localhost:8080/api/coupon/create" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"count\": 10}"
- 생성된 쿠폰중 하나를 사용자에게 지급 (/api/coupon/allocate)
- 사용자 email을 통해서 지급하게 구현
ex) curl -X POST "http://localhost:8080/api/coupon/allocate" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\"}"
- 사용자에게 지급된 쿠폰을 조회 (/api/coupon/user/coupon)
- 사용자 email을 통해서 지급된 정보를 조회
ex) curl -X POST "http://localhost:8080/api/coupon/user/coupon" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A" -H "Content-Type: application/json" -d "{ \"email\": \"test@test.com\"}"
- 지급된 쿠폰중 하나를 사용 (/api/coupon/use/{code})
ex) curl -X PUT "http://localhost:8080/api/coupon/use/725602046346592256" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"
- 지급된 쿠폰중 하나를 사용 취소 (/api/coupon/cancel/{code})
ex) curl -X PUT "http://localhost:8080/api/coupon/cancel/725602046350786563" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회 (/api/coupon/expired)
ex) curl -X GET "http://localhost:8080/api/coupon/expired" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkzMDY3MjU0LCJleHAiOjE1OTMwNzQ0NTR9.j8I34qKqtGzm9jWEl-id5W075s3zmRp4_KubMbCT61mDz2ociGYDBaFHymgNtlT2Na8VYl3mkIv46M3pzJ057A"

### 매일 배치로 동작
- Scheduler works every 1 am in KST
- 만료된 쿠폰이 있으면 그 쿠폰을 만료 상태로 바꿔줌
- Scheduler works every 8 am in KST
- 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능


### 문제 해결 전략
- 쿠폰은 유니크 해야 하므로 uuid를 이용해서 생성했음
- 패스워드는 BCrypt로 암호화
- jwt를 통한 인증 구현
- 필터를 이용해서 token이 invalid하거나 만료됐는 지 체크
- 필요한 fail exception 처리 구현 